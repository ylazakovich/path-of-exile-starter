#!/bin/bash

if [[ "${CI:-}" == "true" ]]; then
  export GRADLE_OPTS="-Dorg.gradle.console=plain"
else
  export GRADLE_OPTS="-Dorg.gradle.console=rich"
fi

# Blue
info() {
  echo -e "\033[1;34mInfo: $1\033[0m"
}

# Yellow
warning() {
  echo -e "\033[1;33mWarning: $1\033[0m"
}

# Red
error() {
  echo -e "\033[1;31mError: $1\033[0m"
}

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
  warning "Failed to determine host platform — compose will choose an appropriate one automatically."
fi

DOCKER_CMD="docker compose -f tools/docker/docker-compose.yml -f tools/docker/docker-compose.override.yml up -d --quiet-pull "${SERVICES[@]}""
source ./tools/scripts/dev/docker_health_check.sh "$DOCKER_CMD"