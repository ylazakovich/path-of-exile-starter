package io.starter.service;

import java.util.List;
import java.util.Objects;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.constants.Emoji;
import io.starter.constants.League;
import io.starter.dao.UserDao;
import io.starter.entity.LeagueEntity;
import io.starter.generator.messages.EditMessageGenerator;
import io.starter.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.generator.replykeyboard.rows.InlineKeyboardRowGenerator;
import io.starter.model.aggregator.Skill;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
@AllArgsConstructor
public class CallbackAnswerService {

  private final DataAccessService dataAccessService;
  private final UserDao userDao;
  private final SettingsService settingsService;

  public EditMessageText onClickSetting(CallbackQuery callbackQuery) {
    League league = League.byCallbackState(CallbackState.byData(callbackQuery.getData()));
    userDao.saveLeague(callbackQuery.getFrom(), Objects.requireNonNull(league));
    InlineKeyboardMarkup inlineKeyboard = settingsService.generateKeyboard();
    return EditMessageGenerator.generate(
        callbackQuery.getMessage(),
        settingsService.generateInlineMessage(callbackQuery.getFrom()),
        inlineKeyboard);
  }

  public EditMessageText onClickSkills(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    List<Skill> skills = dataAccessService.findAllSkills(leagueEntity);
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
      long chaosEquivalentProfit = Math.round(skills.get(i).getChaosEquivalentProfit());
      builder
          .append(skills.get(i).getName())
          .append(Constants.General.SEPARATER)
          .append("%d %s".formatted(chaosEquivalentProfit, "c"))
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
}
