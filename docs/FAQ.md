# FAQ

## Does Telegram bot work over Webhook?

Yes. The bot uses Spring webhook integration.

## Which source is used for market prices?

The project uses [poe.ninja](https://poe.ninja/) as the primary market data source.

## How to run the bot locally?

Use helper scripts:

```bash
bash tools/scripts/dev/run_app.sh
```

Stop services with:

```bash
bash tools/scripts/dev/stop_app.sh
```

## Do I need to call Telegram API manually to register webhook?

No. Webhook subscription is handled by the application startup flow.

## Which database is used?

MariaDB with Flyway migrations.

## How is project stability controlled?

GitHub Actions pipelines run checks on pull requests and on `main` branch.

## Are dependency versions updated automatically?

Yes. The repository uses Renovate for automated dependency updates.
