#!/bin/bash
docker compose \
  -f tools/docker/docker-compose.yml \
  -f tools/docker/docker-compose.override.yml \
   down -v
