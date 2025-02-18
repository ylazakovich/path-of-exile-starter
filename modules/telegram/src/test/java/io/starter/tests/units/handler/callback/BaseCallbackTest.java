package io.starter.tests.units.handler.callback;

import io.starter.telegram.cache.CallbackCache;
import io.starter.telegram.cache.MessageCache;
import io.starter.telegram.dao.SkillDao;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.service.CallbackAnswerService;
import io.starter.telegram.service.MessageAnswerService;
import io.starter.telegram.service.SettingsService;
import io.starter.tests.BaseTest;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.testng.annotations.BeforeClass;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public abstract class BaseCallbackTest extends BaseTest {

  protected final SettingsService settingsService = mock(SettingsService.class);
  protected final SkillDao skillDao = mock(SkillDao.class);
  protected final UserDao userDao = mock(UserDao.class);
  protected final MaybeInaccessibleMessage message = mock(MaybeInaccessibleMessage.class);
  protected final CallbackQuery callbackQuery = mock(CallbackQuery.class);
  protected final Update update = mock(Update.class);
  protected final User user = mock(User.class);

  protected final CallbackAnswerService callbackAnswerService = spy(new CallbackAnswerService(
      skillDao,
      userDao,
      settingsService));
  protected final MessageAnswerService messageAnswerService = spy(new MessageAnswerService(settingsService));
  protected final CallbackCache callbackCache = spy(CallbackCache.class);
  protected final MessageCache messageCache = spy(MessageCache.class);

  @BeforeClass
  protected void setUp() {
    when(update.hasCallbackQuery()).thenReturn(true);
    when(update.getCallbackQuery()).thenReturn(callbackQuery);
    when(callbackQuery.getFrom()).thenReturn(user);
    when(callbackQuery.getMessage()).thenReturn(message);
    doNothing().when(userDao).saveWhenNotExist(user);
    doNothing().when(userDao).saveLastMessageId(user, message);
  }
}
