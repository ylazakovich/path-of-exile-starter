package io.starter.units.handler;

import io.starter.telegram.cash.CallbackCash;
import io.starter.telegram.cash.MessageCash;
import io.starter.telegram.constants.Constants;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.handler.UpdateHandler;
import io.starter.telegram.model.telegram.TelegramFacade;
import io.starter.telegram.service.CallbackAnswerService;
import io.starter.telegram.service.MessageAnswerService;

import net.datafaker.Faker;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.starter.telegram.cash.state.MessageState.SETTINGS;
import static io.starter.telegram.cash.state.MessageState.START;
import static io.starter.telegram.cash.state.MessageState.WELCOME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MessageHandlerTest {

  private final CallbackAnswerService callbackAnswerService = mock(CallbackAnswerService.class);
  private final UserDao userDao = mock(UserDao.class);
  private final Message message = mock(Message.class);
  private final Update update = mock(Update.class);
  private final User user = mock(User.class);

  private final MessageAnswerService messageAnswerService = spy(MessageAnswerService.class);
  private final CallbackCash callbackCash = spy(CallbackCash.class);
  private final MessageCash messageCash = spy(MessageCash.class);

  private final Faker faker = new Faker();

  @BeforeClass
  void setUp() {
    when(update.hasCallbackQuery()).thenReturn(false);
    when(update.getMessage()).thenReturn(message);
    when(message.getFrom()).thenReturn(user);
    doNothing().when(userDao).saveWhenNotExist(user);
    doNothing().when(userDao).saveLastMessageId(user, message);
  }

  @Test(description = "Bot should welcome user when he writes command '/start'")
  void testWhenBotGotCommandStart() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCash, messageCash));
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
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCash, messageCash));

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
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCash, messageCash));

    when(message.getText()).thenReturn(SETTINGS.value);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    SendMessage expected = messageAnswerService.onClickSettings(message);
    SendMessage actual = (SendMessage) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(SendMessage.PATH);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should ignore not supporting messages")
  void testWhenBotGotUnknownCommand() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCash, messageCash));
    String text = faker.text().text();

    when(message.getText()).thenReturn(text);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    assertThat(botApiMethod).isNull();
  }
}
