#!/bin/bash

echo "🚀 Starting Flyway container..."
docker compose up -d --quiet-pull flyway
echo "✅ Flyway container started successfully"
