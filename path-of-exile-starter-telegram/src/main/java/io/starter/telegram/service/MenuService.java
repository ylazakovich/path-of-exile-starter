package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
public class MenuService {

  public SendMessage startMenu(final Message message) {
    final InlineKeyboardMarkup keyboard = getMainMenuKeyboard(message.getFrom());
    return createMessageWithInlineKeyboard(message.getChatId(), keyboard);
  }

  public SendMessage menuWithSkills(final Message message) {
    final InlineKeyboardMarkup keyboard = getSkillsSubMenu(message.getFrom());
    return createMessageWithInlineKeyboard(message.getChatId(), keyboard);
  }

  private SendMessage createMessageWithInlineKeyboard(final long chatId,
                                                      final InlineKeyboardMarkup keyboard) {
    final SendMessage sendMessage = buildDefault("What options do you want to choose ?", chatId);
    if (keyboard != null) {
      sendMessage.setReplyMarkup(keyboard);
    }
    return sendMessage;
  }

  private InlineKeyboardMarkup getMainMenuKeyboard(final User user) {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    InlineKeyboardButton serviceBtn = new InlineKeyboardButton("Service menu");
    InlineKeyboardButton profileBtn = new InlineKeyboardButton("Profile settings");
    InlineKeyboardButton feedbackBtn = new InlineKeyboardButton("Feedback");
    serviceBtn.setCallbackData("service_call");
    profileBtn.setCallbackData("profile_settings_call");
    feedbackBtn.setCallbackData("feedback_call");
    List<InlineKeyboardButton> buttons = List.of(serviceBtn, profileBtn, feedbackBtn);
    keyboard.add(buttons);
    markupInline.setKeyboard(keyboard);
    return markupInline;
  }

  private InlineKeyboardMarkup getSkillsSubMenu(final User user) {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
    InlineKeyboardButton allBtn = new InlineKeyboardButton("ALL");
    InlineKeyboardButton anyBtn = new InlineKeyboardButton("ANY");
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
    return null;
  }
}
