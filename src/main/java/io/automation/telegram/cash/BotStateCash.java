package io.automation.telegram.cash;

import io.automation.telegram.model.BotState;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
//Used to save state bot.
public class BotStateCash {
    private final Map<Long, BotState> botStateMap = new HashMap<>();

    public Map<Long, BotState> getBotStateMap() {
        return botStateMap;
    }

    public void saveBotState(long userId, BotState botState) {
        botStateMap.put(userId, botState);
    }
}
