package io.starter.service;

import java.util.List;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.constants.League;
import io.starter.dao.UserDao;
import io.starter.entity.LeagueEntity;
import io.starter.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.generator.replykeyboard.rows.InlineKeyboardRowGenerator;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
@AllArgsConstructor
public class SettingsService {

  private final UserDao userDao;

  public String generateInlineMessage(User user) {
    LeagueEntity leagueEntity = userDao.readLeague(user);
    String empty = StringUtils.EMPTY;
    return switch (League.byId(leagueEntity.getId())) {
      case STANDARD -> Constants.Settings.ANSWER_FORMAT.formatted("⭐", empty, empty, empty);
      case LEAGUE -> Constants.Settings.ANSWER_FORMAT.formatted(empty, "⭐", empty, empty);
      case HARDCORE -> Constants.Settings.ANSWER_FORMAT.formatted(empty, empty, "⭐", empty);
      case LEAGUE_HARDCORE -> Constants.Settings.ANSWER_FORMAT.formatted(empty, empty, empty, "⭐");
    };
  }

  public InlineKeyboardMarkup generateKeyboard() {
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
