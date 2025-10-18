HEALTH_TIMEOUT="${HEALTH_TIMEOUT:-120}"

get_docker_compose_file() {
  cmd="$1"
  echo "$cmd" | awk '{
    for (i=1; i<=NF; i++) if ($i=="-f") {print $(i+1); exit}
  }'
}

get_services() {
  compose_file="$1"
  docker compose -f "$compose_file" config --services 2>/dev/null
}

wait_for_container_health() {
  cid="$1"
  timeout="${2:-$HEALTH_TIMEOUT}"
  interval=2
  waited=0

  health_check="$(docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null || echo "")"
  if [ -z "$health_check" ] || [ "$health_check" = "null" ]; then
    echo "NO_HEALTHCHECK"
    return 0
  fi

  while :; do
    status="$(docker inspect --format='{{.State.Health.Status}}' "$cid" 2>/dev/null || echo "unknown")"
    case "$status" in
      healthy)   echo "healthy";   return 0 ;;
      unhealthy) echo "unhealthy"; return 0 ;;
      starting|unknown)
        if [ "$waited" -ge "$timeout" ]; then
          echo "$status"
          return 0
        fi
        sleep "$interval"
        waited=$((waited+interval))
        ;;
      *)
        echo "$status"
        return 0
        ;;
    esac
  done
}

check_service_health() {
  service="$1"
  timeout="${2:-$HEALTH_TIMEOUT}"
  failed=0
  any=0

  for cid in $(docker ps -q --filter "label=com.docker.compose.service=$service"); do
    any=1
    result="$(wait_for_container_health "$cid" "$timeout")"

    if [ "$result" = "NO_HEALTHCHECK" ]; then
      warning "Healthcheck is not configured for service '$service' (container $cid)."
      continue
    fi

    if [ "$result" = "healthy" ]; then
      info "Service '$service' is healthy (container $cid)."
      continue
    fi

    if [ "$result" = "starting" ] || [ "$result" = "unknown" ]; then
      warning "Service '$service' did not reach 'healthy' in ${timeout}s (container $cid). State.Health.Status: $result"
      docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      failed=1
      continue
    fi

    if [ "$result" = "unhealthy" ]; then
      warning "Service '$service' is unhealthy (container $cid). Showing health logs:"
      docker inspect --format='{{json .State.Health}}' "$cid" 2>/dev/null | jq
      failed=1
    fi
  done

  if [ "$any" -eq 0 ]; then
    warning "Service '$service' has no running containers yet; skipping healthcheck for it."
  fi

  if [ "$failed" -ne 0 ]; then
    error "Service '$service' healthcheck failed!!!"
    return 1
  fi
  return 0
}

execute() {
  if [ $# -lt 1 ]; then
    error "No docker command provided. Usage: $0 '<docker compose command>' [--timeout N|-t N]"
    exit 1
  fi

  command="$1"
  shift || true

  while [ $# -gt 0 ]; do
    case "$1" in
      --timeout=*)
        HEALTH_TIMEOUT="${1#*=}"
        shift
        ;;
      --timeout|-t)
        shift
        if [ $# -eq 0 ]; then
          error "Missing value for --timeout"
          exit 1
        fi
        HEALTH_TIMEOUT="$1"
        shift
        ;;
      *)
        warning "Unknown argument: $1"
        shift
        ;;
    esac
  done

  if ! [[ "$HEALTH_TIMEOUT" =~ ^[0-9]+$ ]]; then
    error "Invalid timeout value: '$HEALTH_TIMEOUT' (must be non-negative integer seconds)"
    exit 1
  fi

  compose_file="$(get_docker_compose_file "$command")"
  [ -n "$compose_file" ] || compose_file="docker-compose.yml"

  echo ""
  info "Using global healthcheck timeout (per service): ${HEALTH_TIMEOUT}s"
  echo ""

  echo "Starting application..."
  services="$(get_services "$compose_file" || true)"
  if [ -z "$services" ]; then
    error "No services found in $compose_file."
    exit 1
  fi

  echo ""
  printf '──────────────────────────────────────────────\n'
  info "Detected services in $compose_file:"
  printf '──────────────────────────────────────────────\n'
  i=1
  while IFS= read -r svc; do
    [ -n "$svc" ] || continue
    printf "  %2d. %s\n" "$i" "$svc"
    i=$((i+1))
  done <<EOF
$services
EOF
  printf '──────────────────────────────────────────────\n\n'

  sh -c "$command" >/dev/null 2>&1 || { error "Docker compose has not started"; exit 1; }

  echo "Checking health status of services..."
  for service in $services; do
    if docker ps -q --filter "label=com.docker.compose.service=$service" | grep -q . ; then
      if ! check_service_health "$service" "$HEALTH_TIMEOUT"; then
        exit 1
      fi
    fi
  done
  echo "Application started successfully!"
}

execute "$@"
