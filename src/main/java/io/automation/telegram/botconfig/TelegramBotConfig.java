package io.automation.telegram.botconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {
    @Value("${telegrambot.webHookPath}")
    private String webHookPath;
    @Value("${telegrambot.userName}")
    private String userName;
    @Value("${telegrambot.botToken}")
    private String botToken;

    public String getWebHookPath() {
        return webHookPath;
    }

    public TelegramBotConfig setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TelegramBotConfig setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getBotToken() {
        return botToken;
    }

    public TelegramBotConfig setBotToken(String botToken) {
        this.botToken = botToken;
        return this;
    }
}
