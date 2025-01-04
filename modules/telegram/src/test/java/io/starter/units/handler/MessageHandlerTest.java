package io.starter.units.handler;

import java.util.List;

import io.starter.telegram.cash.CallbackCash;
import io.starter.telegram.cash.MessageCash;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.constants.Constants;
import io.starter.telegram.constants.Emoji;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.handler.UpdateHandler;
import io.starter.telegram.model.telegram.TelegramFacade;
import io.starter.telegram.service.CallbackAnswerService;
import io.starter.telegram.service.MessageAnswerService;
import io.starter.telegram.utils.generator.replykeyboard.ReplyKeyboardGenerator;

import net.datafaker.Faker;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.testng.annotations.Test;

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

  @Test(description = "Bot should welcome user when click '/start'")
  public void testWhenUserClickStart() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCash, messageCash));
    String firstName = faker.name().firstName();
    String welcomeMessage = Constants.General.WELCOME.formatted(Emoji.WAVING_HAND, firstName);
    ReplyKeyboardMarkup keyboard = ReplyKeyboardGenerator.replyMenu(
        List.of(MessageState.START.value, MessageState.SETTINGS.value),
        List.of(MessageState.FEEDBACK.value));

    when(update.hasCallbackQuery()).thenReturn(false);
    when(update.getMessage()).thenReturn(message);
    when(message.getFrom()).thenReturn(user);
    when(message.getText()).thenReturn(WELCOME.value);
    when(user.getId()).thenReturn(1L);
    when(user.getFirstName()).thenReturn(firstName);
    doNothing().when(userDao).saveWhenNotExist(user);
    doNothing().when(userDao).saveLastMessageId(user, message);

    SendMessage response = (SendMessage) bot.handleOnUpdate(update);
    assertThat(response.getMethod()).isEqualTo(SendMessage.PATH);
    assertThat(response.getParseMode()).isEqualTo(ParseMode.MARKDOWN);
    assertThat(response.getText()).isEqualTo(welcomeMessage);
    assertThat(response.getReplyMarkup()).isEqualTo(keyboard);
  }
}
