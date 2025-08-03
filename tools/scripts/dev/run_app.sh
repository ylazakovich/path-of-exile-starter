#!/bin/bash

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
docker compose -f tools/docker/docker-compose.yml up -d --quiet-pull "${SERVICES[@]}" || {
  error "Docker compose has not started"
  exit 1
}

echo "Checking health status of services..."
for service in "${SERVICES[@]}"; do
  container_id=$(docker ps --format "{{.ID}} {{.Names}}" | grep -E "^.*$service(-1)$" | awk '{print $1}')
  if [ -z "$container_id" ]; then
    continue
  fi

  health_check=$(docker inspect --format='{{json .State.Health}}' "$container_id" 2>/dev/null || echo "null")
  if [ "$health_check" == "null" ]; then
    warning "Healthcheck is not configured for service '$service'."
    continue
  fi

  status=$(docker inspect --format='{{.State.Health.Status}}' "$container_id" 2>/dev/null || echo "unknown")
  if [ "$status" == "healthy" ]; then
    info "Service '$service' is healthy."
  else
    warning "Service '$service' is not healthy (State.Health.Status: $status). Showing logs:"
    docker inspect --format='{{json .State.Health}}' "$container_id" | jq
    error "Service '$service' healthcheck failed!!!"
    exit 1
  fi
done
echo "Application started successfully!"
