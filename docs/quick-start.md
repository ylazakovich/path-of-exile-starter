# Quick Start

## Prerequisites

- Java 25
- Docker + Docker Compose
- Telegram bot token and webhook URL

## Configure environment

Create or update `.env` in project root and define bot settings:

```bash
TELEGRAM_BOT_TOKEN=<your_bot_token>
TELEGRAM_BOT_WEBHOOK=<public_https_webhook_url>
```

Configuration keys are read by `BotConfiguration` from environment variables.

## Run locally

Start services and Spring apps with the helper script:

```bash
bash tools/scripts/dev/run_app.sh
```

Stop all local services:

```bash
bash tools/scripts/dev/stop_app.sh
```

## Manual Docker run (alternative)

```bash
docker compose \
  -f tools/docker/docker-compose.yml \
  -f tools/docker/docker-compose.override.yml \
  up -d --quiet-pull spring-webflux-aggregator spring-telegram-webhook
```

## Next step

After startup, open the Telegram bot and follow [How To Work With The Bot](guides/how_to_interact_with_skills.md).
