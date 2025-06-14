package io.starter.tests.units.handler.callback.skills;

import java.util.List;
import java.util.stream.IntStream;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.constants.League;
import io.starter.dataproviders.CallbackHandlerProvider;
import io.starter.entity.LeagueEntity;
import io.starter.handler.UpdateHandler;
import io.starter.model.aggregator.Skill;
import io.starter.model.telegram.TelegramFacade;
import io.starter.tests.units.handler.callback.BaseCallbackTest;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class InteractionsTest extends BaseCallbackTest {

  @Test(description = "Bot should react on clicking button 'settings'",
      dataProviderClass = CallbackHandlerProvider.class,
      dataProvider = "whenUserInSettingsMenu")
  void testUserInteractionInSettingsMenu(CallbackState state) {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String callbackQueryId = String.valueOf(faker.number().positive());

    when(callbackQuery.getData()).thenReturn(state.value);
    when(callbackQuery.getId()).thenReturn(callbackQueryId);
    LeagueEntity leagueEntity = new LeagueEntity();
    String msg = Constants.Settings.ANSWER_FORMAT;
    String empty = StringUtils.EMPTY;
    switch (state) {
      case SETTING_STANDARD:
        leagueEntity.setId(League.STANDARD.id);
        when(userDao.readLeague(user)).thenReturn(leagueEntity);
        when(settingsService.generateInlineMessage(user)).thenReturn(msg.formatted("⭐", empty, empty, empty));
        return;
      case SETTING_LEAGUE:
        leagueEntity.setId(League.LEAGUE.id);
        when(userDao.readLeague(user)).thenReturn(leagueEntity);
        when(settingsService.generateInlineMessage(user)).thenReturn(msg.formatted(empty, "⭐", empty, empty));
        return;
      case SETTING_HARDCORE:
        leagueEntity.setId(League.HARDCORE.id);
        when(userDao.readLeague(user)).thenReturn(leagueEntity);
        when(settingsService.generateInlineMessage(user)).thenReturn(msg.formatted(empty, empty, "⭐", empty));
        return;
      case SETTING_LEAGUE_HARDCORE:
        leagueEntity.setId(League.LEAGUE_HARDCORE.id);
        when(userDao.readLeague(user)).thenReturn(leagueEntity);
        when(settingsService.generateInlineMessage(user)).thenReturn(msg.formatted(empty, empty, empty, "⭐"));
        return;
    }
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    EditMessageText expected = callbackAnswerService.onClickSetting(callbackQuery);
    EditMessageText actual = (EditMessageText) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(EditMessageText.PATH);
    assertThat(actual.getText()).contains("Your Current League");
    assertThat(actual.getText()).doesNotContain("empty", "null");
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should react on clicking button options in 'skills' menu",
      dataProviderClass = CallbackHandlerProvider.class,
      dataProvider = "whenUserInSkillsMenu")
  void testUserInteractionInSkillsMenu(CallbackState state) {
    LeagueEntity leagueEntity = mock(LeagueEntity.class);
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String callbackQueryId = String.valueOf(faker.number().positive());
    List<Skill> skills = IntStream.range(0, 20)
        .mapToObj(i -> Skill.builder()
            .name(faker.harryPotter().spell())
            .chaosEquivalentProfit(faker.number().numberBetween(5, 100))
            .build())
        .toList();
    int lastPage = (int) Math.ceil((double) skills.size() / 10);
    int page = faker.number().numberBetween(1, lastPage);

    when(callbackQuery.getData()).thenReturn(state.value);
    when(callbackQuery.getId()).thenReturn(callbackQueryId);
    when(userDao.readLeague(user)).thenReturn(leagueEntity);
    when(dataAccessService.findAllSkills(leagueEntity)).thenReturn(skills);
    when(userDao.readSkillPage(user)).thenReturn(page);
    doNothing().when(userDao).saveSkillPage(user, page);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    EditMessageText expected = callbackAnswerService.onClickSkills(callbackQuery);
    EditMessageText actual = (EditMessageText) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(EditMessageText.PATH);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should ignore not supporting interaction")
  void testWhenUserMadeUnknownInteraction() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String text = faker.text().text();

    when(callbackQuery.getData()).thenReturn(text);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    assertThat(botApiMethod).isNull();
  }
}
