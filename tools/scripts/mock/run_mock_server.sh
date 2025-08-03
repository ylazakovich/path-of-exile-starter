#!/bin/bash

echo "🚀 Starting MockServer container..."
docker compose -f tools/docker/docker-compose.yml up -d --quiet-pull mock-server

sleep=2
max_count=30
count=0
status=0

while true; do
  status=$(curl -o /dev/null -s -w "%{http_code}" http://localhost:1080/mockserver/dashboard)

  if [[ "$status" -eq 200 ]]; then
    echo "✅ MockServer container started successfully"
    break
  fi

  echo "⏳ Waiting for MockServer... ${count}s elapsed, status code: $status"
  sleep "$sleep"
  count=$((count + sleep))

  if [[ "$count" -gt "$max_count" ]]; then
    echo -e "\e[1;31m❌ MockServer did not start within ${max_count}s (http://localhost:1080)\e[0m"
    exit 1
  fi
done
