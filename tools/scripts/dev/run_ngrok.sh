#!/bin/bash

export COMPOSE_PROJECT_NAME="path-of-exile-starter"

echo "🚀 Starting ngrok container..."
docker compose \
  -f tools/docker/docker-compose.yml \
  -f tools/docker/docker-compose.override.yml \
  up -d --quiet-pull ngrok
echo "✅ Ngrok container started successfully"
