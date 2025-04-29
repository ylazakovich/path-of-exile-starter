package io.starter.generator.messages;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@UtilityClass
public class EditMessageGenerator {

  public static EditMessageText generate(MaybeInaccessibleMessage message,
                                         String text) {
    return EditMessageText.builder()
        .chatId(message.getChatId())
        .messageId(message.getMessageId())
        .parseMode("Markdown")
        .text(text)
        .build();
  }

  public static EditMessageText generate(MaybeInaccessibleMessage message,
                                         String text,
                                         InlineKeyboardMarkup keyboard) {
    EditMessageText editMessage = generate(message, text);
    editMessage.setReplyMarkup(keyboard);
    return editMessage;
  }
}
