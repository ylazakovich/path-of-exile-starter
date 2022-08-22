package io.automation.telegram.model;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class TelegramBot extends SpringWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    private TelegramFacade telegramFacade;

    public TelegramBot(TelegramFacade telegramFacade, DefaultBotOptions options, SetWebhook setWebhook) {
        super(options, setWebhook);
        this.telegramFacade = telegramFacade;
    }
    public TelegramBot(TelegramFacade telegramFacade, SetWebhook setWebhook) {
        super(setWebhook);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public TelegramBot setBotPath(String botPath) {
        this.botPath = botPath;
        return this;
    }

    public TelegramBot setBotUsername(String botUsername) {
        this.botUsername = botUsername;
        return this;
    }

    public TelegramBot setBotToken(String botToken) {
        this.botToken = botToken;
        return this;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }
}
