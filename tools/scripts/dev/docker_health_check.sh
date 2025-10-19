#!/bin/bash
set -euo pipefail

DOCKER_HEALTH_TIMEOUT="${DOCKER_HEALTH_TIMEOUT:-120}"

info() { echo -e "\033[1;34mInfo: $1\033[0m"; }
warning() { echo -e "\033[1;33mWarning: $1\033[0m"; }
error() { echo -e "\033[1;31mError: $1\033[0m"; }

join_quoted() {
  local out=() a
  for a in "$@"; do
    printf -v a '%q' "$a"
    out+=("$a")
  done
  printf '%s' "${out[*]}"
}

extract_compose_files_and_project_dir() {
  local -a args=("$@")
  local -a files=()
  local project=""
  local i
  for ((i = 0; i < ${#args[@]}; i++)); do
    if [[ "${args[i]}" == "-f" && $((i + 1)) -lt ${#args[@]} ]]; then
      files+=("${args[i + 1]}")
      ((i++))
      continue
    fi
    if [[ "${args[i]}" == "--project-directory" && $((i + 1)) -lt ${#args[@]} ]]; then
      project="${args[i + 1]}"
      ((i++))
      continue
    fi
  done
  if ((${#files[@]} == 0)); then files=("docker-compose.yml"); fi
  printf '%s\0' "$project" "${files[@]}"
}

build_compose_cmd_array() {
  local project="$1"
  shift
  local -a files=("$@")
  local -a cmd=(docker compose)
  if [[ -n "$project" ]]; then cmd+=(--project-directory "$project"); fi
  local f
  for f in "${files[@]}"; do cmd+=(-f "$f"); done
  printf '%s\0' "${cmd[@]}"
}

get_services_via_config() {
  local project="$1"
  shift
  local -a files=("$@")
  local -a base
  readarray -d '' -t base < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" config --services 2>/dev/null
}

get_services_via_ps() {
  local project="$1"
  shift
  local -a files=("$@")
  local -a base
  readarray -d '' -t base < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" ps --services --all 2>/dev/null || true
}

get_ps_json() {
  local project="$1"
  shift
  local -a files=("$@")
  local -a base
  readarray -d '' -t base < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" ps --format json --all 2>/dev/null
}

wait_for_container_health() {
  local cid="$1"
  local timeout="${2:-$DOCKER_HEALTH_TIMEOUT}"
  local interval=2
  local waited=0
  local hc
  hc="$(docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null || echo "")"
  if [[ -z "$hc" || "$hc" == "null" ]]; then
    echo "NO_HEALTHCHECK"
    return 0
  fi
  while :; do
    local status
    status="$(docker inspect --format='{{.State.Health.Status}}' "$cid" 2>/dev/null || echo "unknown")"
    case "$status" in
      healthy)
        echo "healthy"
        return 0
        ;;
      unhealthy)
        echo "unhealthy"
        return 0
        ;;
      starting | unknown)
        if ((waited >= timeout)); then
          echo "$status"
          return 0
        fi
        sleep "$interval"
        waited=$((waited + interval))
        ;;
      *)
        echo "$status"
        return 0
        ;;
    esac
  done
}

check_service_health() {
  local service="$1"
  local timeout="${2:-$DOCKER_HEALTH_TIMEOUT}"
  local failed=0
  local any=0
  local -a cids=()
  local waited_c=0 interval_c=2
  while :; do
    mapfile -t cids < <(docker ps -q --filter "label=com.docker.compose.service=$service")
    if ((${#cids[@]} > 0)); then
      break
    fi
    if ((waited_c >= timeout)); then
      break
    fi
    sleep "$interval_c"
    waited_c=$((waited_c + interval_c))
  done
  local cid result
  for cid in "${cids[@]}"; do
    [[ -n "$cid" ]] || continue
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
      if command -v jq >/dev/null 2>&1; then
        docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      else
        warning "'jq' not found; showing raw health JSON"
        docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null
      fi
      failed=1
      continue
    fi
    if [[ "$result" == "unhealthy" ]]; then
      warning "Service '$service' is unhealthy (container $cid). Showing health logs:"
      if command -v jq >/dev/null 2>&1; then
        docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      else
        warning "'jq' not found; showing raw health JSON"
        docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null
      fi
      failed=1
    fi
  done
  if ((any == 0)); then
    warning "Service '$service' has no running containers yet; skipping healthcheck for it."
  fi
  if ((failed != 0)); then
    error "Service '$service' healthcheck failed!!!"
    return 1
  fi
  return 0
}

print_command_pretty() {
  local -a a=("$@")
  echo -e "\033[1;34mInfo: Launch command:\033[0m"

  local lines=()
  local i=0

  if ((${#a[@]} >= 2)) && [[ "${a[0]}" == "docker" && "${a[1]}" == "compose" ]]; then
    lines+=("docker compose")
    i=2
  fi

  while ((i < ${#a[@]})); do
    case "${a[i]}" in
      -f | --file | --profile | --project-directory | --wait-timeout | --project-name | -p)
        if ((i + 1 < ${#a[@]})); then
          lines+=("  ${a[i]} ${a[i + 1]}")
          i=$((i + 2))
        else
          lines+=("  ${a[i]}")
          i=$((i + 1))
        fi
        ;;
      up)
        local seg=("up")
        i=$((i + 1))
        while ((i < ${#a[@]})); do
          case "${a[i]}" in
            -[a-zA-Z]* | --[a-zA-Z0-9_-]*)
              if [[ "${a[i]}" == "--wait-timeout" || "${a[i]}" == "--profile" || "${a[i]}" == "--project-directory" || "${a[i]}" == "-f" || "${a[i]}" == "--file" || "${a[i]}" == "--project-name" || "${a[i]}" == "-p" ]]; then
                break
              fi
              seg+=("${a[i]}")
              i=$((i + 1))
              ;;
            *)
              break
              ;;
          esac
        done
        lines+=("  ${seg[*]}")
        ;;
      *)
        if [[ "${a[i]}" != -* ]]; then
          local svcs=()
          while ((i < ${#a[@]})) && [[ "${a[i]}" != -* ]]; do
            svcs+=("${a[i]}")
            i=$((i + 1))
          done
          lines+=("  ${svcs[*]}")
        else
          lines+=("  ${a[i]}")
          i=$((i + 1))
        fi
        ;;
    esac
  done

  local last=$((${#lines[@]} - 1))
  local j
  for ((j = 0; j < last; j++)); do
    printf '%s \\\n' "${lines[j]}"
  done
  printf '%s\n' "${lines[last]}"
}

execute() {
  if (($# < 1)); then
    error "No docker command provided. Example: docker compose -f a.yml up -d [--timeout N|-t N]"
    exit 1
  fi

  local -a cmd_args=()
  while (($# > 0)); do
    case "$1" in
      --timeout=*)
        DOCKER_HEALTH_TIMEOUT="${1#*=}"
        shift
        ;;
      --timeout | -t)
        shift
        if (($# == 0)); then
          error "Missing value for --timeout"
          exit 1
        fi
        DOCKER_HEALTH_TIMEOUT="$1"
        shift
        ;;
      *)
        cmd_args+=("$1")
        shift
        ;;
    esac
  done

  if ! [[ "$DOCKER_HEALTH_TIMEOUT" =~ ^[0-9]+$ ]]; then
    error "Invalid timeout value: '$DOCKER_HEALTH_TIMEOUT'"
    exit 1
  fi

  # Intentionally avoid re-splitting a single string into args; it breaks quoted values.

  local has_up=false has_wait=false has_wait_timeout=false a
  for a in "${cmd_args[@]}"; do
    [[ "$a" == "up" ]] && has_up=true
    [[ "$a" == "--wait" ]] && has_wait=true
    [[ "$a" == "--wait-timeout" || "$a" == --wait-timeout=* ]] && has_wait_timeout=true
  done
  if $has_up; then
    $has_wait || cmd_args+=(--wait)
    if ! $has_wait_timeout; then
      # Use the same timeout knob for compose wait-timeout
      cmd_args+=(--wait-timeout "$DOCKER_HEALTH_TIMEOUT")
    fi
  fi

  local -a proj_and_files=()
  while IFS= read -r -d '' item; do proj_and_files+=("$item"); done < <(extract_compose_files_and_project_dir "${cmd_args[@]}")
  local project="${proj_and_files[0]:-}"
  local -a files=("${proj_and_files[@]:1}")

  echo
  info "Using global healthcheck timeout (per service): ${DOCKER_HEALTH_TIMEOUT}s"
  echo

  local services
  services="$(get_services_via_config "$project" "${files[@]}" || true)"
  if [[ -z "$services" && -n "${SERVICES_LIST:-}" ]]; then
    warning "No services returned by 'config --services'. Falling back to SERVICES_LIST."
    services="$SERVICES_LIST"
  fi

  print_command_pretty "${cmd_args[@]}"

  if ! output="$("${cmd_args[@]}" 2>&1)"; then
    rc=$?
    error "Docker compose failed to start (exit $rc):"
    printf '%s\n' "$output"
    exit "$rc"
  fi

  if [[ -z "$services" ]]; then
    services="$(get_services_via_ps "$project" "${files[@]}" || true)"
  fi
  if [[ -z "$services" ]]; then
    error "Could not determine services even after start."
    local -a diag=(docker compose)
    [[ -n "$project" ]] && diag+=(--project-directory "$project")
    local f
    for f in "${files[@]}"; do diag+=(-f "$f"); done
    "${diag[@]}" config || true
    exit 1
  fi

  local started_services="" up_services="" completed_ok_services="" exited_bad_services=""
  if command -v jq >/dev/null 2>&1; then
    local ps_json
    ps_json="$(get_ps_json "$project" "${files[@]}" || true)"

    if [[ -n "$ps_json" ]]; then
      started_services="$(
        jq -sr 'map(select(type=="object") | .Service) | unique | join(" ")' <<<"$ps_json"
      )"
      up_services="$(
        jq -sr 'map(select(type=="object" and ((.State // .Status)=="running")) | .Service) | unique | join(" ")' <<<"$ps_json"
      )"
      completed_ok_services="$(
        jq -sr 'map(select(type=="object" and ((.State // .Status)=="exited") and ((.ExitCode // 0)|tostring)=="0") | .Service) | unique | join(" ")' <<<"$ps_json"
      )"
      exited_bad_services="$(
        jq -sr 'map(select(type=="object" and ((.State // .Status)=="exited") and ((.ExitCode // 0)|tostring)!="0") | .Service) | unique | join(" ")' <<<"$ps_json"
      )"
    fi
  fi

  [[ -z "$started_services" ]] && started_services="$(get_services_via_ps "$project" "${files[@]}" || true)"

  local to_check="$up_services"
  [[ -z "$to_check" ]] && to_check="$started_services"

  printf '──────────────────────────────────────────────\n'
  info "Detected services:"
  printf '──────────────────────────────────────────────\n'
  local maxlen=0
  while IFS= read -r line; do
    [[ -n "$line" ]] || continue
    ((${#line} > maxlen)) && maxlen=${#line}
  done <<<"$(tr ' ' '\n' <<<"$services")"
  local idx=1 line tag
  while IFS= read -r line; do
    [[ -n "$line" ]] || continue
    tag="[SKIP]"
    if echo " $up_services " | grep -qw "$line"; then
      tag="[UP]"
    elif echo " $completed_ok_services " | grep -qw "$line"; then
      tag="[DONE]"
    elif echo " $exited_bad_services " | grep -qw "$line"; then
      tag="[EXIT-FAIL]"
    elif echo " $started_services " | grep -qw "$line"; then
      tag="[STARTED]"
    fi
    printf "  %2d. %-*s  %s\n" "$idx" "$maxlen" "$line" "$tag"
    idx=$((idx + 1))
  done <<<"$(tr ' ' '\n' <<<"$services")"
  printf '──────────────────────────────────────────────\n\n'

  if [[ -n "$exited_bad_services" ]]; then
    error "Some one-shot services exited with non-zero code: $exited_bad_services"
    exit 1
  fi

  echo "Checking health status of services (running only)..."
  local svc
  while IFS= read -r svc; do
    [[ -n "$svc" ]] || continue
    if ! check_service_health "$svc" "$DOCKER_HEALTH_TIMEOUT"; then
      exit 1
    fi
  done <<<"$(tr ' ' '\n' <<<"$to_check")"

  echo "Application started successfully!"
}
execute "$@"
