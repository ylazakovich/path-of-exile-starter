package io.automation.service;

import java.util.ArrayList;
import java.util.List;

import io.automation.cash.BotStateCash;
import io.automation.dao.UserDAO;
import io.automation.entity.UserEntity;
import io.automation.model.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Service
public class MenuService {

  private final UserDAO userDAO;
  private final BotStateCash stateCash;

  public MenuService(UserDAO userDAO,
                     BotStateCash stateCash) {
    this.userDAO = userDAO;
    this.stateCash = stateCash;
  }

  public SendMessage getMainMenuMessage(final User user,
                                        final long chatId,
                                        final String textMessage) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(user);
    return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
  }

  public SendMessage getSkillsMenu(final User user,
                                   final long chatId,
                                   final String selectAnyCommand) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = getSkillsSubMenu(user);
    return createMessageWithKeyboard(chatId, selectAnyCommand, replyKeyboardMarkup);
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

  private ReplyKeyboardMarkup getMainMenuKeyboard(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = configure();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow skillsRow = new KeyboardRow();
    skillsRow.add(new KeyboardButton("Skills"));
    keyboard.add(skillsRow);
    replyKeyboardMarkup.setKeyboard(keyboard);
    stateCash.saveBotState(user.getId(), State.SKILLS_EVENT);
    UserEntity userEntity = new UserEntity();
    userEntity.setUserId(user.getId());
    userEntity.setName(user.getUserName());
    userDAO.save(userEntity);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup getSkillsSubMenu(final User user) {
    final ReplyKeyboardMarkup replyKeyboardMarkup = configure();
    List<KeyboardRow> keyboard = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();
    row.add(new KeyboardButton("ALL"));
    row.add(new KeyboardButton("ANY"));
    row.add(new KeyboardButton("TODO SPECIFIC"));
    keyboard.add(row);
    replyKeyboardMarkup.setKeyboard(keyboard);
    stateCash.saveBotState(user.getId(), State.SKILLS_WAIT_COMMNAND_EVENT);
    return replyKeyboardMarkup;
  }

  private ReplyKeyboardMarkup configure() {
    final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    replyKeyboardMarkup.setSelective(true);
    replyKeyboardMarkup.setResizeKeyboard(true);
    replyKeyboardMarkup.setOneTimeKeyboard(false);
    return replyKeyboardMarkup;
  }
}
