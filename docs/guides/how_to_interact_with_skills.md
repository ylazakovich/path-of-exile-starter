# How To Work With The Bot

This guide describes both user flow in Telegram and developer flow for maintaining the same behavior in code.

## User flow

### 1. Open the bot and start dialog

- Open [@poe_consultant_bot](https://t.me/poe_consultant_bot)
- Use `/start`
- Bot responds with the main keyboard

### 2. Use **Skills**

- Tap `Skills` from the main menu
- Use arrows (`left`/`right`) to paginate items
- Use refresh button to reload current page
- Use `Link to guide` to open this documentation page

### 3. Use **Vendor Recipes**

- Tap `Vendor Recipes`
- Navigate recipes page by page
- Review ingredients and price values for the selected league

### 4. Use **Settings**

- Open `Settings`
- Choose one of available leagues
- Bot stores selected league and uses it in next calculations

## Developer flow

### Entry points

- Webhook controller receives Telegram update payload.
- Update handler routes message/callback to dedicated services.

Key classes:

- `modules/spring-telegram-webhook/src/main/java/io/starter/controller/WebhookController.java`
- `modules/spring-telegram-webhook/src/main/java/io/starter/handler/UpdateHandler.java`
- `modules/spring-telegram-webhook/src/main/java/io/starter/service/CallbackAnswerService.java`
- `modules/spring-telegram-webhook/src/main/java/io/starter/service/MessageAnswerService.java`

### Where skill guide link is configured

`Constants.Start.SKILLS_GUIDE_LINK` is used by `CallbackAnswerService` for the inline button `Link to guide`.

### Add or change callback behavior

1. Add a callback state in `CallbackState`
2. Handle it in `UpdateHandler`
3. Build message/keyboard in `CallbackAnswerService` or `MessageAnswerService`
4. Add/adjust unit tests under `modules/spring-telegram-webhook/src/test/java/io/starter/tests/units/handler`

### Debug checklist

- Verify `TELEGRAM_BOT_TOKEN` and `TELEGRAM_BOT_WEBHOOK`
- Ensure local services are healthy in Docker
- Inspect application logs from both modules
- Confirm callback payload value matches expected `CallbackState`
