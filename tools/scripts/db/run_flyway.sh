#!/bin/bash

echo "🚀 Starting Flyway container..."
docker compose -f tools/docker/docker-compose.yml up -d --quiet-pull flyway
echo "✅ Flyway container started successfully"
