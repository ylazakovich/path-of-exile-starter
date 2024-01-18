package io.starter.cash;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.starter.model.State;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Service
public class BotStateCash {

  private final Map<Long, State> botStateMap = new HashMap<>();

  public void saveState(long userId, State state) {
    botStateMap.put(userId, state);
  }

  public State getCurrentState(User user) {
    return Objects.nonNull(botStateMap.get(user.getId()))
        ? botStateMap.get(user.getId())
        : State.START;
  }
}
