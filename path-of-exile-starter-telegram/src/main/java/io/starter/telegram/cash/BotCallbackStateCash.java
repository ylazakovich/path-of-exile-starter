package io.starter.telegram.cash;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.starter.telegram.model.State;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Service
public class BotCallbackStateCash {

  private final Map<Long, State.Callback> botStateMap = new HashMap<>();

  public void saveState(Message message, State.Callback state) {
    saveState(message.getFrom().getId(), state);
  }

  public void saveState(long userId, State.Callback state) {
    botStateMap.put(userId, state);
  }

  public State.Callback getCurrentState(User user) {
    return Objects.nonNull(botStateMap.get(user.getId()))
        ? botStateMap.get(user.getId())
        : State.Callback.NO_CMD;
  }
}
