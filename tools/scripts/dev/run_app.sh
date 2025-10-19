#!/bin/bash
set -euo pipefail

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
source "$SCRIPT_DIR/../lib/logging.sh"

if [[ "${CI:-}" == "true" ]]; then
  export GRADLE_OPTS="-Dorg.gradle.console=plain"
else
  export GRADLE_OPTS="-Dorg.gradle.console=rich"
fi

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

CMD=(docker compose -f "$COMPOSE_FILE_A")
CMD+=(-f "$COMPOSE_FILE_B")
CMD+=(up -d --quiet-pull)
CMD+=("${SERVICES[@]}")

extra_args=()

if [[ -n "${DOCKER_HEALTH_TIMEOUT:-}" ]]; then
  extra_args+=(--timeout "$DOCKER_HEALTH_TIMEOUT")
fi

if ! bash "$SCRIPT_DIR/docker_health_check.sh" "${CMD[@]}" "${extra_args[@]}"; then
  error "Application failed to start successfully."
  exit 1
fi
