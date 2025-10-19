#!/bin/bash
set -euo pipefail

# ─────────────────────────────────────────────────────────────────────────────
# Section 0. Bootstrap & settings
# ─────────────────────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/logging.sh"

DOCKER_HEALTH_TIMEOUT="${DOCKER_HEALTH_TIMEOUT:-120}"

# ─────────────────────────────────────────────────────────────────────────────
# Section 1. Compose helpers (args/command builders)
# ─────────────────────────────────────────────────────────────────────────────
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
  ((${#files[@]} == 0)) && files=("docker-compose.yml")
  printf '%s\0' "$project" "${files[@]}"
}

build_compose_cmd_array() {
  local project="$1"
  shift
  local -a files=("$@")
  local -a cmd=(docker compose)
  [[ -n "$project" ]] && cmd+=(--project-directory "$project")
  local file
  for file in "${files[@]}"; do cmd+=(-f "$file"); done
  printf '%s\0' "${cmd[@]}"
}

get_services_via_config() {
  local project="$1"; shift
  local -a files=("$@")
  local -a base=()
  while IFS= read -r -d '' x; do base+=( "$x" ); done < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" config --services 2>/dev/null
}

get_services_via_ps() {
  local project="$1"; shift
  local -a files=("$@")
  local -a base=()
  while IFS= read -r -d '' x; do base+=( "$x" ); done < <(build_compose_cmd_array "$project" "${files[@]}")
  local out
  if ! out="$("${base[@]}" ps --services --all 2>/dev/null)"; then
    error "Failed to list services via 'docker compose ps'. Check project directory and compose files."
    return 1
  fi
  [[ -z "${out//[[:space:]]/}" ]] && return 1
  printf '%s\n' "$out"
}

get_ps_json() {
  local project="$1"; shift
  local -a files=("$@")
  local -a base=()
  while IFS= read -r -d '' x; do base+=( "$x" ); done < <(build_compose_cmd_array "$project" "${files[@]}")
  "${base[@]}" ps --format json --all 2>/dev/null
}

# ─────────────────────────────────────────────────────────────────────────────
# Section 2. Health helpers
# ─────────────────────────────────────────────────────────────────────────────
wait_for_container_health() {
  local cid="$1"
  local timeout="${2:-$DOCKER_HEALTH_TIMEOUT}"
  local interval=2 waited=0
  local hc
  hc="$(docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null || echo "")"
  [[ -z "$hc" || "$hc" == "null" ]] && {
    echo "NO_HEALTHCHECK"
    return 0
  }
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
        ((waited >= timeout)) && {
          echo "$status"
          return 0
        }
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
  local failed=0 any=0
  local -a cids=()
  local waited_c=0 interval_c=2
  local project_filter=()
  [[ -n "${COMPOSE_PROJECT_NAME:-}" ]] && project_filter+=(--filter "label=com.docker.compose.project=${COMPOSE_PROJECT_NAME}")

  while :; do
    mapfile -t cids < <(docker ps --no-trunc -q "${project_filter[@]}" --filter "label=com.docker.compose.service=$service")
    ((${#cids[@]} > 0)) && break
    ((waited_c >= timeout)) && break
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

  ((any == 0)) && warning "Service '$service' has no running containers yet; skipping healthcheck for it."
  ((failed != 0)) && {
    error "Service '$service' healthcheck failed!!!"
    return 1
  }
  return 0
}

# ─────────────────────────────────────────────────────────────────────────────
# Section 3. Pretty printers
# ─────────────────────────────────────────────────────────────────────────────
print_command_pretty() {
  local -a a=("$@")
  info "Launch command:"
  local lines=() i=0
  if ((${#a[@]} >= 2)) && [[ "${a[0]}" == "docker" && "${a[1]}" == "compose" ]]; then
    lines+=("docker compose")
    i=2
  fi
  q() { printf %q "$1"; }
  while ((i < ${#a[@]})); do
    case "${a[i]}" in
      -f | --f | --profile | --project-directory | --wait-timeout | --project-name | -p)
        if ((i + 1 < ${#a[@]})); then
          lines+=("  $(q "${a[i]}") $(q "${a[i + 1]}")")
          i=$((i + 2))
        else
          lines+=("  $(q "${a[i]}")")
          i=$((i + 1))
        fi
        ;;
      up)
        local seg=("up")
        i=$((i + 1))
        while ((i < ${#a[@]})); do
          case "${a[i]}" in
            -[a-zA-Z]* | --[a-zA-Z0-9_-]*)
              case "${a[i]}" in --wait-timeout | --profile | --project-directory | -f | --f | --project-name | -p) break ;; esac
              seg+=("$(q "${a[i]}")")
              i=$((i + 1))
              ;;
            *) break ;;
          esac
        done
        lines+=("  ${seg[*]}")
        ;;
      *)
        if [[ "${a[i]}" != -* ]]; then
          local svcs=()
          while ((i < ${#a[@]})) && [[ "${a[i]}" != -* ]]; do
            svcs+=("$(q "${a[i]}")")
            i=$((i + 1))
          done
          lines+=("  ${svcs[*]}")
        else
          lines+=("  $(q "${a[i]}")")
          i=$((i + 1))
        fi
        ;;
    esac
  done
  local last=$((${#lines[@]} - 1))
  local j
  for ((j = 0; j < last; j++)); do printf '%s \\\n' "${lines[j]}"; done
  printf '%s\n' "${lines[last]}"
}

print_detected_services_table() {
  local services="$1" up="$2" done_s="$3" bad_s="$4" started="$5"
  printf '──────────────────────────────────────────────\n'
  info "Detected services:"
  printf '──────────────────────────────────────────────\n'
  local maxlen=0 line
  while IFS= read -r line; do
    [[ -n "$line" ]] || continue
    ((${#line} > maxlen)) && maxlen=${#line}
  done <<<"$(tr ' ' '\n' <<<"$services")"
  local idx=1 tag
  while IFS= read -r line; do
    [[ -n "$line" ]] || continue
    tag="[SKIP]"
    echo " $up " | grep -qw "$line" && tag="[UP]"
    echo " $done_s " | grep -qw "$line" && tag="[DONE]"
    echo " $bad_s " | grep -qw "$line" && tag="[EXIT-FAIL]"
    if [[ "$tag" == "[SKIP]" ]] && echo " $started " | grep -qw "$line"; then tag="[STARTED]"; fi
    printf "  %2d. %-*s  %s\n" "$idx" "$maxlen" "$line" "$tag"
    idx=$((idx + 1))
  done <<<"$(tr ' ' '\n' <<<"$services")"
  printf '──────────────────────────────────────────────\n\n'
}

# ─────────────────────────────────────────────────────────────────────────────
# Section 4. Main flow
# ─────────────────────────────────────────────────────────────────────────────
execute() {
  # 4.1 parse args
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
        (("$#" > 0)) || {
          error "Missing value for --timeout"
          exit 1
        }
        DOCKER_HEALTH_TIMEOUT="$1"
        shift
        ;;
      *)
        cmd_args+=("$1")
        shift
        ;;
    esac
  done
  [[ "$DOCKER_HEALTH_TIMEOUT" =~ ^[0-9]+$ ]] || {
    error "Invalid timeout value: '$DOCKER_HEALTH_TIMEOUT'"
    exit 1
  }

  # 4.2 normalize & enrich args
  local has_up=false has_wait=false has_wait_timeout=false a
  for a in "${cmd_args[@]}"; do
    [[ "$a" == "up" ]] && has_up=true
    [[ "$a" == "--wait" ]] && has_wait=true
    [[ "$a" == "--wait-timeout" || "$a" == --wait-timeout=* ]] && has_wait_timeout=true
  done
  if $has_up; then
    $has_wait || cmd_args+=(--wait)
    $has_wait_timeout || cmd_args+=(--wait-timeout "$DOCKER_HEALTH_TIMEOUT")
  fi

  # 4.3 compose context
  local -a ctx=()
  while IFS= read -r -d '' x; do ctx+=("$x"); done < <(extract_compose_files_and_project_dir "${cmd_args[@]}")
  local project="${ctx[0]:-}"
  local -a files=("${ctx[@]:1}")
  [[ -z "${COMPOSE_PROJECT_NAME:-}" && -n "$project" ]] && export COMPOSE_PROJECT_NAME="$(basename "$project")"

  echo
  info "Using global healthcheck timeout (per service): ${DOCKER_HEALTH_TIMEOUT}s"
  echo

  # 4.4 list declared services
  local services
  services="$(get_services_via_config "$project" "${files[@]}" || true)"
  if [[ -z "$services" && -n "${SERVICES_LIST:-}" ]]; then
    warning "No services returned by 'config --services'. Falling back to SERVICES_LIST."
    services="$SERVICES_LIST"
  fi

  # 4.5 print and run compose (with tee to temp log)
  print_command_pretty "${cmd_args[@]}"
  {
    tmp_out="$(mktemp -t compose_out.XXXXXX)" || {
      error "Failed to create temporary file for logging."
      exit 1
    }
    cleanup_tmp() { rm -f -- "$tmp_out"; }
    trap cleanup_tmp EXIT
    if ! "${cmd_args[@]}" 2>&1 | tee "$tmp_out"; then
      local rc_left=${PIPESTATUS[0]:-1}
      error "Docker compose failed to start (exit $rc_left):"
      printf '--- docker compose output (last 200 lines) ---\n'
      tail -n 200 -- "$tmp_out" || true
      exit "$rc_left"
    fi
    cleanup_tmp
    trap - EXIT
  }

  # 4.6 if nothing declared — show diag and stop
  if [[ -z "$services" ]]; then
    error "Could not determine services even after start."
    printf '--- diagnostics ---\n'
    printf 'COMPOSE_PROJECT_NAME=%s\n' "${COMPOSE_PROJECT_NAME:-<unset>}"
    printf 'project-directory=%s\n' "${project:-<unset>}"
    printf 'compose-fs:\n'
    local file
    for file in "${files[@]}"; do printf '  - %s\n' "$file"; done
    printf '--- docker compose config ---\n'
    local -a diag=(docker compose)
    [[ -n "$project" ]] && diag+=(--project-directory "$project")
    for file in "${files[@]}"; do diag+=(-f "$file"); done
    printf '\n--- docker compose ps --all ---\n'
    "${diag[@]}" ps --all || true
    exit 1
  fi

  # 4.7 collect runtime sets
  local started_services="" up_services="" completed_ok_services="" exited_bad_services=""
  if command -v jq >/dev/null 2>&1; then
    local ps_json
    ps_json="$(get_ps_json "$project" "${files[@]}" || true)"
    if [[ -n "$ps_json" ]]; then
      started_services="$(jq -sr 'map(select(type=="object") | .Service) | unique | join(" ")' <<<"$ps_json")"
      up_services="$(jq -sr 'map(select(type=="object" and ((.State // .Status)=="running")) | .Service) | unique | join(" ")' <<<"$ps_json")"
      completed_ok_services="$(jq -sr 'map(select(type=="object" and ((.State // .Status)=="exited") and ((.ExitCode // 0)|tostring)=="0") | .Service) | unique | join(" ")' <<<"$ps_json")"
      exited_bad_services="$(jq -sr 'map(select(type=="object" and ((.State // .Status)=="exited") and ((.ExitCode // 0)|tostring)!="0") | .Service) | unique | join(" ")' <<<"$ps_json")"
    fi
  fi
  [[ -z "$started_services" ]] && started_services="$(get_services_via_ps "$project" "${files[@]}" || true)"

  # 4.8 print table
  print_detected_services_table "$services" "$up_services" "$completed_ok_services" "$exited_bad_services" "$started_services"

  # 4.9 fail fast on exited!=0 one-shots
  if [[ -n "$exited_bad_services" ]]; then
    error "Some one-shot services exited with non-zero code: $exited_bad_services"
    exit 1
  fi

  # 4.10 health-check only running
  echo "Checking health status of services (running only)..."
  local to_check="$up_services"
  [[ -z "$to_check" ]] && to_check="$started_services"
  local svc
  while IFS= read -r svc; do
    [[ -n "$svc" ]] || continue
    check_service_health "$svc" "$DOCKER_HEALTH_TIMEOUT" || exit 1
  done <<<"$(tr ' ' '\n' <<<"$to_check")"

  echo "Application started successfully!"
}

# ─────────────────────────────────────────────────────────────────────────────
# Section 5. Entrypoint
# ─────────────────────────────────────────────────────────────────────────────
execute "$@"
