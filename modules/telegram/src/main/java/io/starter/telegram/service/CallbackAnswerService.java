package io.starter.telegram.service;

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
import io.starter.telegram.utils.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.telegram.utils.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.telegram.utils.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

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

  // TODO: not finished
  public EditMessageText onClickSkills(CallbackQuery callbackQuery) {
    LeagueEntity leagueEntity = userDao.readLeague(callbackQuery.getFrom());
    List<Skill> skills = skillDao.readAll(leagueEntity);
    int page = 0;
    String inlineMessage = toPaginatedMessage(page, skills);
    InlineKeyboardMarkup keyboard = onClickSkills(page);
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  // TODO: not finished
  private InlineKeyboardMarkup onClickSkills(int page) {
    List<InlineKeyboardButton> headerButtons = List.of(
        InlineKeyboardButtonGenerator.generate("Назад", "page:" + (page - 1)),
        InlineKeyboardButtonGenerator.generate(String.valueOf(page), "current page"),
        InlineKeyboardButtonGenerator.generate("Вперед", "page:" + (page + 1))
    );
    List<InlineKeyboardButton> footerButtons = List.of(
        InlineKeyboardButtonGenerator.generate(
            Emoji.REPEAT.value,
            CallbackState.REFRESH_SKILLS.value
        )
    );
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(headerButtons, footerButtons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private String toPaginatedMessage(int page, List<Skill> skills) {
    final int itemsPerPage = 10;
    int start = page * itemsPerPage;
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
}
