#!/bin/bash

CONTAINER_NAME="ngrok"
docker stop "$CONTAINER_NAME" && docker rm "$CONTAINER_NAME"
