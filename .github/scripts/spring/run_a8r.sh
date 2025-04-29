#!/bin/bash
./gradlew :modules:a8r:bootRun &

echo "Service will started about few minutes"
count=0
sleep=5
max_count=300
status=0

until [ $status -eq 200 ]; do
  if [[ $count -gt $max_count ]]; then
    echo "Service was not started on http://localhost:8080/health/ping"
    exit 1
  fi
  status=$(curl -o /dev/null -s -w "%{http_code}" http://localhost:8080/health/ping)
  echo "Service... $((count += $sleep)) seconds, status code - $status"
  sleep $sleep
done
echo "Service is started on http://localhost:8080"
