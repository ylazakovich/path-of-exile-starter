package io.automation.telegram.cash;

import java.util.HashMap;
import java.util.Map;

import io.automation.telegram.model.State;
import org.springframework.stereotype.Service;

@Service
//Used to save state bot.
public class BotStateCash {

  private final Map<Long, State> botStateMap = new HashMap<>();

  public Map<Long, State> getBotStateMap() {
    return botStateMap;
  }

  public void saveBotState(long userId, State state) {
    botStateMap.put(userId, state);
  }
}
