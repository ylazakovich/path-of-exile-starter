package io.starter.telegram.service;

import java.util.List;
import java.util.Objects;

import io.starter.telegram.cache.state.CallbackState;
import io.starter.telegram.constants.Constants;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.constants.League;
import io.starter.telegram.dao.SkillDao;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.generator.messages.EditMessageGenerator;
import io.starter.telegram.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.telegram.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.generator.replykeyboard.rows.InlineKeyboardRowGenerator;
import io.starter.telegram.model.aggregator.Skill;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
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

  public EditMessageText onClickSetting(CallbackQuery callbackQuery) {
    League league = League.byCallbackState(CallbackState.byData(callbackQuery.getData()));
    userDao.saveLeague(callbackQuery.getFrom(), Objects.requireNonNull(league));
    InlineKeyboardMarkup inlineKeyboard = keyboardOnClickSettings();
    return EditMessageGenerator.generate(
        callbackQuery.getMessage(),
        inlineMessage(callbackQuery.getFrom()),
        inlineKeyboard);
  }

  public EditMessageText onClickSkills(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    List<Skill> skills = skillDao.readAll(leagueEntity);
    CallbackState callbackState = CallbackState.byData(callbackQuery.getData());
    int page = userDao.readSkillPage(from);
    String inlineMessage = StringUtils.EMPTY;
    if (callbackState == CallbackState.SKILLS || callbackState == CallbackState.REFRESH_SKILLS) {
      inlineMessage = toPaginatedMessage(page, skills);
    }
    if (callbackState == CallbackState.SKILLS_NEXT) {
      page = checkAndSyncPage(++page, skills);
      inlineMessage = toPaginatedMessage(page, skills);
    }
    if (callbackState == CallbackState.SKILLS_PREVIOUS) {
      page = checkAndSyncPage(--page, skills);
      inlineMessage = toPaginatedMessage(page, skills);
    }
    userDao.saveSkillPage(from, page);
    InlineKeyboardMarkup keyboard = onClickSkills(page);
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup onClickSkills(int page) {
    InlineKeyboardButton linkToGuide =
        InlineKeyboardButtonGenerator.generate("Link to guide", CallbackState.NO_CMD.value);
    linkToGuide.setUrl(Constants.Start.SKILLS_GUIDE_LINK);
    List<InlineKeyboardButton> headerButtons = List.of(linkToGuide);
    List<InlineKeyboardButton> bodyButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.LEFT.value, CallbackState.SKILLS_PREVIOUS.value),
        InlineKeyboardButtonGenerator.generate(String.valueOf(page), CallbackState.CURRENT.value),
        InlineKeyboardButtonGenerator.generate(Emoji.RIGHT.value, CallbackState.SKILLS_NEXT.value)
    );
    List<InlineKeyboardButton> footerButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.REPEAT.value, CallbackState.REFRESH_SKILLS.value)
    );
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(headerButtons, bodyButtons, footerButtons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  // TODO: create task for realizing paginated message over Fabric Pattern
  private String toPaginatedMessage(int page, List<Skill> skills) {
    final int itemsPerPage = 10;
    int start = (page - 1) * itemsPerPage;
    int end = Math.min(start + itemsPerPage, skills.size());
    final StringBuilder builder = new StringBuilder();
    for (int i = start; i < end; i++) {
      builder
          .append(skills.get(i).getName())
          .append(Constants.General.SEPARATER)
          .append(Math.round(skills.get(i).getChaosEquivalentProfit()))
          .append(StringUtils.LF);
    }
    return builder.toString();
  }

  private int checkAndSyncPage(int page, List<Skill> skills) {
    final int itemsPerPage = 10;
    int totalPages = (int) Math.ceil((double) skills.size() / itemsPerPage);
    if (page < 1) {
      page = totalPages;
    }
    if (page > totalPages) {
      page = 1;
    }
    return page;
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
}
