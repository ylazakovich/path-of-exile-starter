package io.starter.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.starter.cache.state.CallbackState;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;

@Getter
@Service
public class CallbackCache {

  private final Map<Long, CallbackState> botStateMap = new HashMap<>();

  public void saveState(CallbackQuery query, CallbackState state) {
    saveState(query.getFrom().getId(), state);
  }

  public void saveState(long userId, CallbackState state) {
    botStateMap.put(userId, state);
  }

  public CallbackState getCurrentState(User user) {
    return Objects.nonNull(botStateMap.get(user.getId()))
        ? botStateMap.get(user.getId())
        : CallbackState.NO_CMD;
  }
}
