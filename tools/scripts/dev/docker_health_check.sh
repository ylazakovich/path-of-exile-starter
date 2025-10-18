#!/bin/bash
set -euo pipefail

HEALTH_TIMEOUT="${HEALTH_TIMEOUT:-120}"

info() { echo -e "\033[1;34mInfo: $1\033[0m"; }
warning() { echo -e "\033[1;33mWarning: $1\033[0m"; }
error() { echo -e "\033[1;31mError: $1\033[0m"; }

join_quoted() {
  local out=() a
  for a in "$@"; do printf -v a '%q' "$a"; out+=("$a"); done
  printf '%s' "${out[*]}"
}

extract_compose_files_and_project_dir() {
  local -a args=("$@")
  local -a files=()
  local project=""
  local i
  for ((i=0; i<${#args[@]}; i++)); do
    if [[ "${args[i]}" == "-f" && $((i+1)) -lt ${#args[@]} ]]; then
      files+=("${args[i+1]}"); ((i++)); continue
    fi
    if [[ "${args[i]}" == "--project-directory" && $((i+1)) -lt ${#args[@]} ]]; then
      project="${args[i+1]}"; ((i++)); continue
    fi
  done
  if (( ${#files[@]} == 0 )); then files=("docker-compose.yml"); fi
  printf '%s\0' "$project" "${files[@]}"
}

build_compose_cmd_array() {
  local project="$1"; shift
  local -a files=("$@")
  local -a cmd=(docker compose)
  if [[ -n "$project" ]]; then cmd+=(--project-directory "$project"); fi
  local f; for f in "${files[@]}"; do cmd+=(-f "$f"); done
  printf '%s\0' "${cmd[@]}"
}

get_services_via_config() {
  local project="$1"; shift
  local -a files=("$@")
  local -a base
  readarray -d '' -t base < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" config --services 2>/dev/null
}

get_services_via_ps() {
  local project="$1"; shift
  local -a files=("$@")
  local -a base
  readarray -d '' -t base < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" ps --services 2>/dev/null || true
}

wait_for_container_health() {
  local cid="$1"
  local timeout="${2:-$HEALTH_TIMEOUT}"
  local interval=2
  local waited=0
  local hc
  hc="$(docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null || echo "")"
  if [[ -z "$hc" || "$hc" == "null" ]]; then echo "NO_HEALTHCHECK"; return 0; fi
  while :; do
    local status
    status="$(docker inspect --format='{{.State.Health.Status}}' "$cid" 2>/dev/null || echo "unknown")"
    case "$status" in
      healthy) echo "healthy"; return 0 ;;
      unhealthy) echo "unhealthy"; return 0 ;;
      starting|unknown)
        if (( waited >= timeout )); then echo "$status"; return 0; fi
        sleep "$interval"; waited=$((waited+interval)) ;;
      *) echo "$status"; return 0 ;;
    esac
  done
}

check_service_health() {
  local service="$1"
  local timeout="${2:-$HEALTH_TIMEOUT}"
  local failed=0
  local any=0
  local cid result
  for cid in $(docker ps -q --filter "label=com.docker.compose.service=$service"); do
    any=1
    result="$(wait_for_container_health "$cid" "$timeout")"
    if [[ "$result" == "NO_HEALTHCHECK" ]]; then
      warning "Healthcheck is not configured for service '$service' (container $cid)."
      continue
    fi
    if [[ "$result" == "healthy" ]]; then
      info "Service '$service' is healthy (container $cid)."
      continue
    fi
    if [[ "$result" == "starting" || "$result" == "unknown" ]]; then
      warning "Service '$service' did not reach 'healthy' in ${timeout}s (container $cid). State.Health.Status: $result"
      docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      failed=1
      continue
    fi
    if [[ "$result" == "unhealthy" ]]; then
      warning "Service '$service' is unhealthy (container $cid). Showing health logs:"
      docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      failed=1
    fi
  done
  if (( any == 0 )); then
    warning "Service '$service' has no running containers yet; skipping healthcheck for it."
  fi
  if (( failed != 0 )); then
    error "Service '$service' healthcheck failed!!!"
    return 1
  fi
  return 0
}

execute() {
  if (( $# < 1 )); then
    error "No docker command provided. Example: docker compose -f a.yml up -d [--timeout N|-t N]"
    exit 1
  fi

  local -a cmd_args=()
  while (( $# > 0 )); do
    case "$1" in
      --timeout=*) HEALTH_TIMEOUT="${1#*=}"; shift ;;
      --timeout|-t) shift; if (( $# == 0 )); then error "Missing value for --timeout"; exit 1; fi; HEALTH_TIMEOUT="$1"; shift ;;
      *) cmd_args+=("$1"); shift ;;
    esac
  done

  if ! [[ "$HEALTH_TIMEOUT" =~ ^[0-9]+$ ]]; then
    error "Invalid timeout value: '$HEALTH_TIMEOUT'"
    exit 1
  fi

  if (( ${#cmd_args[@]} == 1 )); then
    IFS=' ' read -r -a cmd_args <<<"${cmd_args[0]}"
  fi

 local -a proj_and_files=()
 while IFS= read -r -d '' item; do
   proj_and_files+=("$item")
 done < <(extract_compose_files_and_project_dir "${cmd_args[@]}")

 local project="${proj_and_files[0]:-}"
 local -a files=("${proj_and_files[@]:1}")

  echo
  info "Using global healthcheck timeout (per service): ${HEALTH_TIMEOUT}s"
  echo

  local services
  services="$(get_services_via_config "$project" "${files[@]}" || true)"

  if [[ -z "$services" && -n "${SERVICES_LIST:-}" ]]; then
    warning "No services returned by 'config --services'. Falling back to SERVICES_LIST."
    services="$SERVICES_LIST"
  fi

  if [[ -z "$services" ]]; then
    warning "No services from config; will start first, then read 'compose ps --services'."
  fi

  local display_cmd
  display_cmd="$(join_quoted "${cmd_args[@]}")"
  info "Launch command: $display_cmd"

  if ! "${cmd_args[@]}" >/dev/null; then
    error "Docker compose has not started"
    exit 1
  fi

  if [[ -z "$services" ]]; then
    services="$(get_services_via_ps "$project" "${files[@]}" || true)"
  fi

  if [[ -z "$services" ]]; then
    error "Could not determine services even after start."
    docker compose $( [[ -n "$project" ]] && printf ' --project-directory %q' "$project" ) $(printf ' -f %q' "${files[@]}") config || true
    exit 1
  fi

  printf '──────────────────────────────────────────────\n'
  info "Detected services:"
  printf '──────────────────────────────────────────────\n'
  local idx=1 line
  while IFS= read -r line; do
    [[ -n "$line" ]] || continue
    printf "  %2d. %s\n" "$idx" "$line"
    idx=$((idx+1))
  done <<<"$(tr ' ' '\n' <<<"$services")"
  printf '──────────────────────────────────────────────\n\n'

  echo "Checking health status of services..."
  local svc
  for svc in $services; do
    if docker ps -q --filter "label=com.docker.compose.service=$svc" | grep -q . ; then
      if ! check_service_health "$svc" "$HEALTH_TIMEOUT"; then
        exit 1
      fi
    fi
  done
  echo "Application started successfully!"
}

execute "$@"
