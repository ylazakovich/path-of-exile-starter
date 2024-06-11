package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.config.Emoji;
import io.starter.telegram.dao.SkillsDao;
import io.starter.telegram.model.aggregator.Skill;

import io.starter.telegram.utils.generator.messages.EditMessageGenerator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class MessageService {

  private final SkillsDao skillsDAO;

  public MessageService(SkillsDao skillsDAO) {
    this.skillsDAO = skillsDAO;
  }

  public EditMessageText messageWithSkills(CallbackQuery callback) {
    final List<Skill> skills = skillsDAO.findAll();
    InlineKeyboardMarkup markup = keyboardWithRefresh();
    return EditMessageGenerator.generate(callback.getMessage(), initBuilder(skills).toString(), markup);
  }

  private InlineKeyboardMarkup keyboardWithRefresh() {
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

  private StringBuilder initBuilder(List<Skill> skills) {
    final StringBuilder builder = new StringBuilder();
    skills.forEach(skill -> builder
        .append(skill.getName())
        .append(" : ")
        .append(Math.round(skill.getChaosEquivalentProfit()))
        .append("\n"));
    return builder;
  }
}
