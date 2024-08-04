package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.starter.telegram.Constants;
import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.dao.SkillDao;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.utils.generator.messages.EditMessageGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class OnCallbackAnswerService {

  private final SkillDao skillDao;

  public OnCallbackAnswerService(SkillDao skillDao) {
    this.skillDao = skillDao;
  }

  public EditMessageText onClickSkills(CallbackQuery callback) {
    List<Skill> skills = skillDao.readAll();
    String inlineMessage = convertSkillsToStringBuilder(skills).toString();
    InlineKeyboardMarkup keyboard = onClickSkills();
    return EditMessageGenerator.generate(callback.getMessage(), inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup onClickSkills() {
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
