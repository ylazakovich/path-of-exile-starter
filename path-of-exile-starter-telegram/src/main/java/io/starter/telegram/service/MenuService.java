package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.List;

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

  public SendMessage getStart(final Message message) {
    final ReplyKeyboardMarkup keyboard = getReplyMenu(message.getFrom());
    return createMessageWithInlineKeyboard(message.getChatId(), keyboard);
  }

  public SendMessage getSkills(final Message message) {
    final InlineKeyboardMarkup keyboard = getSubMenuWithSkills(message.getFrom());
    return createMessageWithInlineKeyboard(message.getChatId(), keyboard);
  }

  private SendMessage createMessageWithInlineKeyboard(final long chatId,
                                                      final ReplyKeyboardMarkup keyboard) {
    final SendMessage sendMessage = buildDefault("What options do you want to choose ?", chatId);
    if (keyboard != null) {
      sendMessage.setReplyMarkup(keyboard);
    }
    return sendMessage;
  }

  private SendMessage createMessageWithInlineKeyboard(final long chatId,
                                                      final InlineKeyboardMarkup keyboard) {
    final SendMessage sendMessage = buildDefault("What options do you want to choose ?", chatId);
    if (keyboard != null) {
      sendMessage.setReplyMarkup(keyboard);
    }
    return sendMessage;
  }

  private ReplyKeyboardMarkup getReplyMenu(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = buildReplyKeyboard();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow skillsRow = new KeyboardRow();
    skillsRow.add(new KeyboardButton("Skills"));
    skillsRow.add(new KeyboardButton("Settings"));
    skillsRow.add(new KeyboardButton("Feedback"));
    keyboard.add(skillsRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
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

  private SendMessage buildDefault(String text, long chatId) {
    final SendMessage message = new SendMessage();
    message.enableMarkdown(true);
    message.setText(text);
    message.setChatId(chatId);
    return message;
  }
}
