HEALTH_TIMEOUT="${HEALTH_TIMEOUT:-120}"

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

extract_compose_files_from_args() {
  local -a args=("$@")
  local -a files=()
  local i
  for ((i=0; i<${#args[@]}; i++)); do
    if [[ "${args[i]}" == "-f" ]]; then
      if (( i+1 < ${#args[@]} )); then
        files+=("${args[i+1]}")
        ((i++))
      fi
    fi
  done
  if (( ${#files[@]} == 0 )); then
    files=("docker-compose.yml")
  fi
  printf '%s\n' "${files[@]}"
}

build_compose_config_cmd() {
  local -a files=("$@")
  local -a cmd=(docker compose)
  local f
  for f in "${files[@]}"; do
    cmd+=(-f "$f")
  done
  cmd+=(config --services)
  printf '%s\0' "${cmd[@]}"
}

get_services() {
  local -a files=("$@")
  local -a cfg_cmd
  IFS=$'\0' read -r -d '' -a cfg_cmd < <(build_compose_config_cmd "${files[@]}")
  "${cfg_cmd[@]}" 2>/dev/null
}

wait_for_container_health() {
  local cid="$1"
  local timeout="${2:-$HEALTH_TIMEOUT}"
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
      healthy) echo "healthy"; return 0 ;;
      unhealthy) echo "unhealthy"; return 0 ;;
      starting|unknown)
        if (( waited >= timeout )); then
          echo "$status"
          return 0
        fi
        sleep "$interval"
        waited=$((waited+interval))
        ;;
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
      --timeout=*)
        HEALTH_TIMEOUT="${1#*=}"; shift ;;
      --timeout|-t)
        shift
        if (( $# == 0 )); then error "Missing value for --timeout"; exit 1; fi
        HEALTH_TIMEOUT="$1"; shift ;;
      *)
        cmd_args+=("$1"); shift ;;
    esac
  done

  if ! [[ "$HEALTH_TIMEOUT" =~ ^[0-9]+$ ]]; then
    error "Invalid timeout value: '$HEALTH_TIMEOUT'"
    exit 1
  fi

  if (( ${#cmd_args[@]} == 1 )); then
    IFS=' ' read -r -a cmd_args <<<"${cmd_args[0]}"
  fi

  local -a compose_files
  mapfile -t compose_files < <(extract_compose_files_from_args "${cmd_args[@]}")

  echo
  info "Using global healthcheck timeout (per service): ${HEALTH_TIMEOUT}s"
  echo

  local services
  if ! services="$(get_services "${compose_files[@]}" || true)"; then
    error "Failed to parse docker compose configuration."
    docker compose $(printf ' -f %q' "${compose_files[@]}") config || true
    exit 1
  fi

  if [[ -z "$services" ]]; then
    error "No services found in $(printf '%s ' "${compose_files[@]}")."
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
  done <<<"$services"
  printf '──────────────────────────────────────────────\n\n'

  local display_cmd
  display_cmd="$(join_quoted "${cmd_args[@]}")"
  info "Launch command: $display_cmd"

  if ! "${cmd_args[@]}" >/dev/null 2>&1; then
    error "Docker compose has not started"
    exit 1
  fi

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