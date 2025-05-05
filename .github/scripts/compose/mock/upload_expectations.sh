#!/bin/bash

MOCKSERVER_URL=${1:-http://localhost:1080}
DIR=${2:-.github/environment/mock/expectations}

find "$DIR" -type f -name "*.json" | while read -r file; do
  printf "\n⏳ Uploading '%s'\n" "$file"
  curl -s -X PUT "$MOCKSERVER_URL/mockserver/expectation" \
    -d @"$file" \
    -H "Content-Type: application/json"
done

printf "\n✅ All expectations uploaded.\n"
