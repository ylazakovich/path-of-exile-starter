#!/bin/bash

echo "🚀 Starting ngrok container..."
docker compose -f tools/docker/docker-compose.yml up -d --quiet-pull ngrok
echo "✅ Ngrok container started successfully"
