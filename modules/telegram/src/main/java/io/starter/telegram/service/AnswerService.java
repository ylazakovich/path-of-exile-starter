package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.starter.telegram.Constants;
import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.config.Emoji;
import io.starter.telegram.dao.SkillsDao;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.utils.generator.messages.EditMessageGenerator;
import io.starter.telegram.utils.generator.messages.SendMessageGenerator;
import io.starter.telegram.utils.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.telegram.utils.generator.replykeyboard.ReplyKeyboardGenerator;
import io.starter.telegram.utils.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.utils.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class AnswerService {

  private final SkillsDao skillsDao;

  public AnswerService(SkillsDao skillsDAO) {
    this.skillsDao = skillsDAO;
  }

  public SendMessage messageOnFirstStart(Message message) {
    List<String> line1 = List.of(MessageState.START.value, MessageState.SETTINGS.value);
    List<String> line2 = List.of(MessageState.FEEDBACK.value);
    String firstName = message.getFrom().getFirstName();
    String inlineMessage = Constants.WELCOME.formatted(Emoji.WAVING_HAND, firstName);
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(line1, line2);
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), keyboard);
  }

  public SendMessage messageOnClickStart(Message message) {
    String inlineMessage = Constants.QUESTION;
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickStart();
    return SendMessageGenerator.generate(inlineMessage, message.getChatId(), inlineKeyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickStart() {
    InlineKeyboardButton button1 = InlineKeyboardButtonGenerator.generate(Constants.SKILLS, CallbackState.SKILLS.value);
    InlineKeyboardButton button2 = InlineKeyboardButtonGenerator
        .generate(Constants.BLESSING, CallbackState.ITEMS_AFTER_BLESSING.value);
    List<InlineKeyboardButton> buttons = List.of(button1, button2);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(buttons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  public EditMessageText messageOnClickSkills(MaybeInaccessibleMessage message) {
    String inlineMessage = Constants.SKILLS_GUIDE;
    InlineKeyboardMarkup keyboard = keyboardOnClickSkills();
    return EditMessageGenerator.generate(message, inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup keyboardOnClickSkills() {
    InlineKeyboardButton button = InlineKeyboardButtonGenerator
        .generate(Constants.ALL_SKILLS, CallbackState.ALL_SKILLS.value);
    List<InlineKeyboardButton> buttons = new ArrayList<>(Collections.singleton(button));
    return InlineKeyboardGenerator.withButtons(buttons);
  }

  public EditMessageText callableMessageOnClickSkills(CallbackQuery callback) {
    List<Skill> skills = skillsDao.findAll();
    String inlineMessage = convertSkillsToStringBuilder(skills).toString();
    InlineKeyboardMarkup keyboard = callableMessageOnClickSkills();
    return EditMessageGenerator.generate(callback.getMessage(), inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup callableMessageOnClickSkills() {
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup(Collections.emptyList());
    List<InlineKeyboardRow> keyboard = new ArrayList<>();
    InlineKeyboardButton refreshBtn = InlineKeyboardButton.builder()
        .text(Emoji.REPEAT.value)
        .callbackData(CallbackState.REFRESH_SKILLS.value)
        .build();
    List<InlineKeyboardButton> buttons = List.of(refreshBtn);
    keyboard.add(new InlineKeyboardRow(buttons));
    markup.setKeyboard(keyboard);
    return markup;
  }

  private StringBuilder convertSkillsToStringBuilder(List<Skill> skills) {
    final StringBuilder builder = new StringBuilder();
    skills.forEach(skill -> builder
        .append(skill.getName())
        .append(Constants.SEPARATER)
        .append(Math.round(skill.getChaosEquivalentProfit()))
        .append(StringUtils.LF));
    return builder;
  }
}
