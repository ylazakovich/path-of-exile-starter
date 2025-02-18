package io.starter.tests.units.handler.message.commands;

import io.starter.telegram.constants.Constants;
import io.starter.telegram.constants.League;
import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.handler.UpdateHandler;
import io.starter.telegram.model.telegram.TelegramFacade;
import io.starter.tests.units.handler.message.BaseMessageTest;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.testng.annotations.Test;

import static io.starter.telegram.cache.state.MessageState.SETTINGS;
import static io.starter.telegram.cache.state.MessageState.START;
import static io.starter.telegram.cache.state.MessageState.WELCOME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CommandsTest extends BaseMessageTest {

  @Test(description = "Bot should welcome user when he writes command '/start'")
  void testWhenBotGotCommandStart() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String firstName = faker.name().firstName();

    when(message.getText()).thenReturn(WELCOME.value);
    when(user.getFirstName()).thenReturn(firstName);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    SendMessage expected = messageAnswerService.onFirstStart(message);
    SendMessage actual = (SendMessage) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(SendMessage.PATH);
    assertThat(actual.getText()).contains(firstName);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should send message about selecting next command")
  void testWhenBotGotCommandExpandOptions() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));

    when(message.getText()).thenReturn(START.value);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    SendMessage expected = messageAnswerService.onClickStart(message);
    SendMessage actual = (SendMessage) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(SendMessage.PATH);
    assertThat(actual.getText()).isEqualTo(Constants.General.QUESTION);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should send message with Settings")
  void testWhenBotGotCommandSettings() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));

    when(this.message.getText()).thenReturn(SETTINGS.value);
    LeagueEntity leagueEntity = new LeagueEntity();
    leagueEntity.setId(League.STANDARD.id);
    when(userDao.readLeague(user)).thenReturn(leagueEntity);
    String msg = Constants.Settings.ANSWER_FORMAT;
    String empty = StringUtils.EMPTY;
    when(settingsService.generateInlineMessage(user)).thenReturn(msg.formatted("⭐", empty, empty, empty));
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    SendMessage expected = messageAnswerService.onClickSettings(this.message);
    SendMessage actual = (SendMessage) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(SendMessage.PATH);
    assertThat(actual.getText()).contains("Your Current League");
    assertThat(actual.getText()).doesNotContain("empty", "null");
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should ignore not supporting messages")
  void testWhenBotGotUnknownCommand() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String text = faker.text().text();

    when(message.getText()).thenReturn(text);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    assertThat(botApiMethod).isNull();
  }
}
