#!/bin/bash
set -euo pipefail

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

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

if REPO_ROOT="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel 2>/dev/null)"; then
  :
else
  REPO_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"
fi

COMPOSE_FILE_A="$REPO_ROOT/tools/docker/docker-compose.yml"
COMPOSE_FILE_B="$REPO_ROOT/tools/docker/docker-compose.override.yml"

if [[ ! -f "$COMPOSE_FILE_A" ]]; then
  error "File not found: $COMPOSE_FILE_A"
  exit 1
fi
if [[ ! -f "$COMPOSE_FILE_B" ]]; then
  warning "File not found: $COMPOSE_FILE_B — using only $COMPOSE_FILE_A"
fi

if ! MERGED_SERVICES="$(docker compose --project-directory "$REPO_ROOT" -f "$COMPOSE_FILE_A" ${COMPOSE_FILE_B:+-f "$COMPOSE_FILE_B"} config --services 2>/dev/null)"; then
  error "Failed to parse docker compose configuration."
  docker compose --project-directory "$REPO_ROOT" -f "$COMPOSE_FILE_A" ${COMPOSE_FILE_B:+-f "$COMPOSE_FILE_B"} config || true
  exit 1
fi

if [[ -z "$MERGED_SERVICES" ]]; then
  warning "No services returned by 'docker compose config --services'. Will fallback to SERVICES array after start."
fi

PROFILES_ARG=()
if [[ -n "${COMPOSE_PROFILES:-}" ]]; then
  IFS=',' read -r -a __profiles <<<"$COMPOSE_PROFILES"
  for p in "${__profiles[@]}"; do
    PROFILES_ARG+=( --profile "$p" )
  done
fi

CMD=( docker compose --project-directory "$REPO_ROOT" -f "$COMPOSE_FILE_A" )
if [[ -f "$COMPOSE_FILE_B" ]]; then
  CMD+=( -f "$COMPOSE_FILE_B" )
fi
CMD+=( "${PROFILES_ARG[@]}" up -d --quiet-pull )
CMD+=( "${SERVICES[@]}" )

SERVICES_LIST="$(printf '%s ' "${SERVICES[@]}")"

export SERVICES_LIST
source "$REPO_ROOT/tools/scripts/dev/docker_health_check.sh" "${CMD[@]}"
