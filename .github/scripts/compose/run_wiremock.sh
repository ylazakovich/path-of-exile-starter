#!/bin/bash

echo "🚀 Starting WireMock container..."
docker compose up -d --quiet-pull mock-server
echo "✅ WireMock container started successfully"
