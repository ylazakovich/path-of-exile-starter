package io.starter.tests.units.handler.message;

import io.starter.cache.CallbackCache;
import io.starter.cache.MessageCache;
import io.starter.dao.UserDao;
import io.starter.service.CallbackAnswerService;
import io.starter.service.MessageAnswerService;
import io.starter.service.SettingsService;
import io.starter.tests.BaseTest;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.testng.annotations.BeforeClass;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public abstract class BaseMessageTest extends BaseTest {

  protected final CallbackAnswerService callbackAnswerService = mock(CallbackAnswerService.class);
  protected final SettingsService settingsService = mock(SettingsService.class);
  protected final UserDao userDao = mock(UserDao.class);
  protected final Message message = mock(Message.class);
  protected final Update update = mock(Update.class);
  protected final User user = mock(User.class);

  protected final MessageAnswerService messageAnswerService = spy(new MessageAnswerService(settingsService));
  protected final CallbackCache callbackCache = spy(CallbackCache.class);
  protected final MessageCache messageCache = spy(MessageCache.class);

  @BeforeClass
  protected void setUp() {
    when(update.hasCallbackQuery()).thenReturn(false);
    when(update.getMessage()).thenReturn(message);
    when(message.getFrom()).thenReturn(user);
    doNothing().when(userDao).saveWhenNotExist(user);
    doNothing().when(userDao).saveLastMessageId(user, message);
  }
}
