package io.automation.telegram.cash;

import java.util.HashMap;
import java.util.Map;

import io.automation.telegram.model.State;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class BotStateCash {

  private final Map<Long, State> botStateMap = new HashMap<>();

  public void saveBotState(long userId, State state) {
    botStateMap.put(userId, state);
  }
}
