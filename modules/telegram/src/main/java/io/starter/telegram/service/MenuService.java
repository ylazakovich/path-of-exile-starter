package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.List;

import io.starter.telegram.cash.state.MessageState;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class MenuService {

  public SendMessage getMain(final Message message) {
    final ReplyKeyboardMarkup keyboard = getReplyMenu(message.getFrom());
    return createMessageWithInlineKeyboard(message, keyboard);
  }

  public SendMessage getStart(final Message message) {
    final InlineKeyboardMarkup keyboard = getSubMenuWithSkills(message.getFrom());
    return createMessageWithInlineKeyboard(message.getChatId(), keyboard);
  }

  private SendMessage createMessageWithInlineKeyboard(final Message message,
                                                      final ReplyKeyboardMarkup keyboard) {
    final SendMessage sendMessage = build(
        """
            \uD83D\uDC4B\
                
            Greetings, Exile **%s**!
            I will tell you the most profitable ways to earn your first Divine.
            """.formatted(message.getFrom().getFirstName()),
        message.getChatId());
    if (keyboard != null) {
      sendMessage.setReplyMarkup(keyboard);
    }
    return sendMessage;
  }

  private SendMessage createMessageWithInlineKeyboard(final long chatId,
                                                      final InlineKeyboardMarkup keyboard) {
    final SendMessage sendMessage = build("What options do you want to choose ?", chatId);
    if (keyboard != null) {
      sendMessage.setReplyMarkup(keyboard);
    }
    return sendMessage;
  }

  private ReplyKeyboardMarkup getReplyMenu(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = buildReplyKeyboard();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow line1 = new KeyboardRow();
    KeyboardRow line2 = new KeyboardRow();
    KeyboardRow line3 = new KeyboardRow();
    line1.add(new KeyboardButton(MessageState.START.value));
    line1.add(new KeyboardButton(MessageState.SETTINGS.value));
    line2.add(new KeyboardButton(MessageState.CLEAN.value));
    line3.add(new KeyboardButton(MessageState.FEEDBACK.value));
    keyboard.add(line1);
    keyboard.add(line2);
    keyboard.add(line3);
    replyKeyboardMarkup.setKeyboard(keyboard);
    replyKeyboardMarkup.setResizeKeyboard(true);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup buildReplyKeyboard() {
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);
    return replyKeyboardMarkup;
  }

  private InlineKeyboardMarkup getSubMenuWithSkills(final User user) {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    InlineKeyboardButton allBtn = new InlineKeyboardButton("ALL");
    InlineKeyboardButton anyBtn = new InlineKeyboardButton("ANY");
    allBtn.setCallbackData("skills_all_pressed");
    anyBtn.setCallbackData("skills_any_pressed");
    List<InlineKeyboardButton> buttons = List.of(allBtn, anyBtn);
    keyboard.add(buttons);
    markupInline.setKeyboard(keyboard);
    return markupInline;
  }

  private SendMessage build(String text, long chatId) {
    final SendMessage message = new SendMessage();
    message.enableMarkdown(true);
    message.setText(text);
    message.setChatId(chatId);
    return message;
  }
}
