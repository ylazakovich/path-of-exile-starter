#!/bin/bash

export COMPOSE_PROJECT_NAME="path-of-exile-starter"

docker compose \
  -f tools/docker/docker-compose.yml \
  -f tools/docker/docker-compose.override.yml \
   down -v
