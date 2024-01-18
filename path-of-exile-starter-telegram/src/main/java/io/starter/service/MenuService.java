package io.starter.service;

import java.util.ArrayList;
import java.util.List;


import io.starter.cash.BotStateCash;
import io.starter.model.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class MenuService {

  public SendMessage startMenu(final Message message) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(message.getFrom());
    return createMessageWithKeyboard(message.getChatId(), replyKeyboardMarkup);
  }

  public SendMessage menuWithSkills(final Message message) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = getSkillsSubMenu(message.getFrom());
    return createMessageWithKeyboard(message.getChatId(), replyKeyboardMarkup);
  }

  private SendMessage createMessageWithKeyboard(final long chatId,
                                                final ReplyKeyboardMarkup replyKeyboardMarkup) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(String.valueOf(chatId));
    sendMessage.setText("What options do you want to choose ?");
    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendMessage;
  }

  private ReplyKeyboardMarkup getMainMenuKeyboard(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = buildReplyKeyboard();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow skillsRow = new KeyboardRow();
    skillsRow.add(new KeyboardButton("Skills"));
    keyboard.add(skillsRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
//    stateCash.saveBotState(user.getId(), State.SKILLS_EVENT);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup getSkillsSubMenu(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = buildReplyKeyboard();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();
    row.add(new KeyboardButton("ALL"));
    row.add(new KeyboardButton("ANY"));
    row.add(new KeyboardButton("TODO SPECIFIC"));
    keyboard.add(row);
    replyKeyboardMarkup.setKeyboard(keyboard);
//    stateCash.saveBotState(user.getId(), State.WAIT_FOR_COMMAND);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup buildReplyKeyboard() {
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);
    return replyKeyboardMarkup;
  }
}
