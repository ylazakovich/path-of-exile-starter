#!/bin/bash

echo "🚀 Starting ngrok container..."
docker compose up -d --quiet-pull ngrok
echo "✅ Ngrok container started successfully"
