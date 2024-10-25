package io.starter.telegram.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.constants.Constants;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.constants.LeagueSetting;
import io.starter.telegram.dao.SkillDao;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.utils.generator.messages.AnswerCallbackQueryGenerator;
import io.starter.telegram.utils.generator.messages.EditMessageGenerator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
public class CallbackAnswerService {

  private final SkillDao skillDao;
  private final UserDao userDao;

  public CallbackAnswerService(SkillDao skillDao,
                               UserDao userDao) {
    this.skillDao = skillDao;
    this.userDao = userDao;
  }

  public AnswerCallbackQuery onClickSetting(CallbackQuery callbackQuery) {
    LeagueSetting leagueSetting = LeagueSetting.byCallbackState(CallbackState.byData(callbackQuery.getData()));
    userDao.saveLeague(callbackQuery.getFrom(), Objects.requireNonNull(leagueSetting));
    return AnswerCallbackQueryGenerator.generateAnswerCallbackQuery(callbackQuery.getId());
  }

  public EditMessageText onClickSkills(CallbackQuery callbackQuery) {
    LeagueEntity leagueEntity = userDao.readLeague(callbackQuery.getFrom());
    List<Skill> skills = skillDao.readAll(leagueEntity);
    String inlineMessage = convertSkillsToStringBuilder(skills).toString();
    InlineKeyboardMarkup keyboard = onClickSkills();
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
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
        .append(Constants.General.SEPARATER)
        .append(Math.round(skill.getChaosEquivalentProfit()))
        .append(StringUtils.LF));
    return builder;
  }
}
