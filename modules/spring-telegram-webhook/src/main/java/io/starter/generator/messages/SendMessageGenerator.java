package io.starter.generator.messages;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@UtilityClass
public class SendMessageGenerator {

  public static SendMessage generate(String text,
                                     long chatId) {
    return SendMessage.builder()
        .chatId(chatId)
        .parseMode("Markdown")
        .text(text)
        .build();
  }

  public static SendMessage generate(String text, long chatId, InlineKeyboardMarkup keyboardMarkup) {
    SendMessage message = generate(text, chatId);
    message.setReplyMarkup(keyboardMarkup);
    return message;
  }

  public static SendMessage generate(String text, long chatId, ReplyKeyboardMarkup keyboard) {
    SendMessage message = generate(text, chatId);
    message.setReplyMarkup(keyboard);
    return message;
  }
}
