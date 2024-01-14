package io.automation.cash;

import java.util.HashMap;
import java.util.Map;

import io.automation.model.State;
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
