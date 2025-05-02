#!/bin/bash

echo "🚀 Starting MockServer container..."
docker compose up -d --quiet-pull mock-server
echo "✅ MockServer container started successfully"
