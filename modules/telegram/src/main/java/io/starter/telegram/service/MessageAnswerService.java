package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.cache.state.CallbackState;
import io.starter.telegram.cache.state.MessageState;
import io.starter.telegram.constants.Constants;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.constants.League;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.generator.messages.SendMessageGenerator;
import io.starter.telegram.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.telegram.generator.replykeyboard.ReplyKeyboardGenerator;
import io.starter.telegram.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class MessageAnswerService {

  private final UserDao userDao;

  public MessageAnswerService(UserDao userDao) {
    this.userDao = userDao;
  }

  public SendMessage onFirstStart(Message message) {
    List<String> line1 = List.of(MessageState.START.value, MessageState.SETTINGS.value);
    List<String> line2 = List.of(MessageState.FEEDBACK.value);
    String firstName = message.getFrom().getFirstName();
    String inlineMessage = Constants.General.WELCOME.formatted(Emoji.WAVING_HAND, firstName);
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(line1, line2);
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), keyboard);
  }

  public SendMessage onClickStart(Message message) {
    String inlineMessage = Constants.General.QUESTION;
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickStart();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickStart() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator
        .generate(Constants.Start.SKILLS, CallbackState.SKILLS.value);
    InlineKeyboardButton button2 = InlineKeyboardButtonGenerator
        .generate(Constants.Start.BLESSING, CallbackState.ITEMS_AFTER_BLESSING.value);
    List<InlineKeyboardButton> buttons = List.of(button1, button2);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(buttons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  public SendMessage onClickSettings(Message message) {
    String inlineMessage = inlineMessage(message.getFrom());
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickSettings();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private String inlineMessage(User user) {
    LeagueEntity leagueEntity = userDao.readLeague(user);
    String empty = StringUtils.EMPTY;
    if (leagueEntity.getId().equals(League.STANDARD.id)) {
      return Constants.Settings.ANSWER_FORMAT.formatted("⭐", empty, empty, empty);
    }
    if (leagueEntity.getId().equals(League.LEAGUE.id)) {
      return Constants.Settings.ANSWER_FORMAT.formatted(empty, "⭐", empty, empty);
    }
    if (leagueEntity.getId().equals(League.HARDCORE.id)) {
      return Constants.Settings.ANSWER_FORMAT.formatted(empty, empty, "⭐", empty);
    }
    if (leagueEntity.getId().equals(League.LEAGUE_HARDCORE.id)) {
      return Constants.Settings.ANSWER_FORMAT.formatted(empty, empty, empty, "⭐");
    }
    throw new RuntimeException("No such league");
  }

  private InlineKeyboardMarkup keyboardOnClickSettings() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator
        .generate(Constants.Settings.STANDARD, CallbackState.SETTING_STANDARD.value);
    InlineKeyboardButton button2 = InlineKeyboardButtonGenerator
        .generate(Constants.Settings.LEAGUE, CallbackState.SETTING_LEAGUE.value);
    InlineKeyboardButton button3 = InlineKeyboardButtonGenerator
        .generate(Constants.Settings.HARDCORE, CallbackState.SETTING_HARDCORE.value);
    InlineKeyboardButton button4 = InlineKeyboardButtonGenerator
        .generate(Constants.Settings.LEAGUE_HARDCORE, CallbackState.SETTING_LEAGUE_HARDCORE.value);
    List<InlineKeyboardButton> row1 = List.of(button1, button2);
    List<InlineKeyboardButton> row2 = List.of(button3, button4);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(row1, row2);
    return InlineKeyboardGenerator.withRows(keyboard);
  }
}
