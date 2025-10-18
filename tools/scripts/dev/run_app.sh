if [[ "${CI:-}" == "true" ]]; then
  export GRADLE_OPTS="-Dorg.gradle.console=plain"
else
  export GRADLE_OPTS="-Dorg.gradle.console=rich"
fi

info() { echo -e "\033[1;34mInfo: $1\033[0m"; }
warning() { echo -e "\033[1;33mWarning: $1\033[0m"; }
error() { echo -e "\033[1;31mError: $1\033[0m"; }

echo "Starting application..."

SERVICES=(
  "spring-webflux-aggregator"
  "spring-telegram-webhook"
)

export DOCKER_BUILDKIT=1

HOST_PLATFORM="$(docker info --format '{{.OSType}}/{{.Architecture}}' || true)"
if [[ -n "${HOST_PLATFORM}" ]]; then
  export DOCKER_DEFAULT_PLATFORM="${HOST_PLATFORM}"
  info "Using local platform: ${DOCKER_DEFAULT_PLATFORM}"
else
  warning "Failed to determine host platform — compose will choose automatically."
fi

COMPOSE_FILE_A="tools/docker/docker-compose.yml"
COMPOSE_FILE_B="tools/docker/docker-compose.override.yml"

if [[ ! -f "$COMPOSE_FILE_A" ]]; then
  error "File not found: $COMPOSE_FILE_A"
  exit 1
fi
if [[ ! -f "$COMPOSE_FILE_B" ]]; then
  warning "File not found: $COMPOSE_FILE_B — using only $COMPOSE_FILE_A"
fi

if ! MERGED_SERVICES="$(docker compose -f "$COMPOSE_FILE_A" ${COMPOSE_FILE_B:+-f "$COMPOSE_FILE_B"} config --services 2>/dev/null)"; then
  error "Failed to parse docker compose configuration."
  docker compose -f "$COMPOSE_FILE_A" ${COMPOSE_FILE_B:+-f "$COMPOSE_FILE_B"} config || true
  exit 1
fi

if [[ -z "$MERGED_SERVICES" ]]; then
  error "No services found in the merged configuration."
  exit 1
fi

for srv in "${SERVICES[@]}"; do
  if ! grep -qx "$srv" <<<"$MERGED_SERVICES"; then
    warning "Service '$srv' not found in merged compose. Available: $(tr '\n' ' ' <<<"$MERGED_SERVICES")"
  fi
done

CMD=( docker compose -f "$COMPOSE_FILE_A" )
if [[ -f "$COMPOSE_FILE_B" ]]; then
  CMD+=( -f "$COMPOSE_FILE_B" )
fi
CMD+=( up -d --quiet-pull )
CMD+=( "${SERVICES[@]}" )

CMD_STR="$(printf '%q ' "${CMD[@]}")"

info "Launch command: $CMD_STR"
source ./tools/scripts/dev/docker_health_check.sh "$CMD_STR"
