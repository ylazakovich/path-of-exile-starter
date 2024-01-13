package io.automation.telegram.model.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.dao.UserDAO;
import io.automation.telegram.entity.UserEntity;
import io.automation.telegram.model.State;
import io.automation.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class EventHandler {

  private final BotStateCash botStateCash;

  private final UserDAO userDAO;
  private final MenuService menuService;

  @Value("${telegram.adminId}")
  private int admin_id;

  @Autowired
  public EventHandler(BotStateCash botStateCash,
                      UserDAO userDAO,
                      MenuService menuService) {
    this.botStateCash = botStateCash;
    this.userDAO = userDAO;
    this.menuService = menuService;
  }

  public SendMessage saveNewUser(Message message,
                                 long userId,
                                 SendMessage sendMessage) {
    String userName = message.getFrom().getUserName();
    UserEntity userEntity = new UserEntity();
    userEntity.id = userId;
    userEntity.name = userName;
    userEntity.on = Boolean.TRUE;
    userDAO.save(userEntity);
    sendMessage.setText("Welcome to path of exile aggregator service");
    botStateCash.saveBotState(userId, State.ENTER_TIME);
    return sendMessage;
  }

  public BotApiMethod<?> onEvent(Message message) {
    UserEntity userEntity = userDAO.findByUserId(message.getFrom().getId());
    boolean on = userEntity.on;
    on = !on;
    userEntity.on = on;
    userDAO.save(userEntity);
    botStateCash.saveBotState(message.getFrom().getId(), State.START);
    return menuService.getMainMenuMessage(message.getChatId(),
        "Handled your message", message.getFrom().getId());
  }

  public BotApiMethod<?> enterLocalTimeUser(Message message) {
    long userId = message.getFrom().getId();
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(String.valueOf(message.getChatId()));
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
    Date nowHour = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(nowHour);
    int num;
    try {
      num = Integer.parseInt(message.getText());
    } catch (NumberFormatException e) {
      sendMessage.setText("Введенные символы не число, посторите ввод");
      return sendMessage;
    }
    if (num < 0 || num > 24) {
      sendMessage.setText("Вы ввели неверное время, повторите.");
      return sendMessage;
    }
    Date userHour;
    try {
      userHour = simpleDateFormat.parse(message.getText());
    } catch (ParseException e) {
      sendMessage.setText("Вы ввели неверное время, повторите.");
      return sendMessage;
    }
    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTime(userHour);
    int serverHour = calendar.get(Calendar.HOUR_OF_DAY);
    int clientHour = calendar1.get(Calendar.HOUR_OF_DAY);
    int timeZone = clientHour - serverHour;
    sendMessage.setText("Ваш часовой пояс: " + "+" + timeZone);
    UserEntity userEntity = userDAO.findByUserId(userId);
    userEntity.timeZone = timeZone;
    userDAO.save(userEntity);
    botStateCash.saveBotState(userId, State.START);
    return sendMessage;
  }

  public BotApiMethod<?> removeUserHandler(Message message, long userId) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(String.valueOf(message.getChatId()));
    UserEntity userEntity;
    try {
      long i = Long.parseLong(message.getText());
      userEntity = userDAO.findByUserId(i);
    } catch (NumberFormatException e) {
      sendMessage.setText("Введенная строка не является числом, попробуйте снова!");
      return sendMessage;
    }
    if (userEntity == null) {
      sendMessage.setText("Введенное число отсутсвует в списке, попробуйте снова!");
      return sendMessage;
    }
    userDAO.removeUser(userEntity);
    botStateCash.saveBotState(userId, State.START);
    sendMessage.setText("Удаление прошло успешно");
    return sendMessage;
  }

  public BotApiMethod<?> allUsers(long userId) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(String.valueOf(userId));
    StringBuilder builder = new StringBuilder();
    List<UserEntity> list = userDAO.findAll();
    for (UserEntity userEntity : list) {
      builder.append(buildUser(userEntity));
    }
    replyMessage.setText(builder.toString());
    replyMessage.setReplyMarkup(menuService.getInlineMessageButtonsAllUser());
    botStateCash.saveBotState(userId, State.START);
    return replyMessage;
  }

  private StringBuilder buildUser(UserEntity userEntity) {
    StringBuilder builder = new StringBuilder();
    builder.append(userEntity.id).append(". ").append(userEntity.name).append("\n");
    return builder;
  }
}
