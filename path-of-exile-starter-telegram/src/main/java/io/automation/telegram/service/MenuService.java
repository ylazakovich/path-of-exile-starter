package io.automation.telegram.service;

import java.util.ArrayList;
import java.util.List;

import io.automation.telegram.dao.UserDAO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class MenuService {

  private final UserDAO userDAO;

  // TODO: how to convert admin id;
//  @Value("#{new Integer('${telegram.adminId}')}")
//  private Integer adminId;

  public MenuService(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  public SendMessage getMainMenuMessage(final long chatId,
                                        final String textMessage,
                                        final long userId) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(userId);
    return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
  }

  private ReplyKeyboardMarkup getMainMenuKeyboard(long userId) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow skillsRow = new KeyboardRow();
    skillsRow.add(new KeyboardButton("Skills"));
    keyboard.add(skillsRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
    return replyKeyboardMarkup;
  }

  private SendMessage createMessageWithKeyboard(final long chatId,
                                                String textMessage,
                                                final ReplyKeyboardMarkup replyKeyboardMarkup) {
    final SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(String.valueOf(chatId));
    sendMessage.setText(textMessage);
    if (replyKeyboardMarkup != null) {
      sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }
    return sendMessage;
  }
}
