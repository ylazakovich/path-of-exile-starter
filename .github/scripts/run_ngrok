#!/bin/bash

export $(grep -v '^#' .env | xargs)

docker run --name=ngrok -it -e NGROK_AUTHTOKEN="${NGROK_AUTHTOKEN}" ngrok/ngrok:latest http host.docker.internal:5050
