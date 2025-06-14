package io.starter.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.starter.cache.state.MessageState;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Getter
@Service
public class MessageCache {

  private final Map<Long, MessageState> botStateMap = new HashMap<>();

  public void saveState(Message message, MessageState state) {
    saveState(message.getFrom().getId(), state);
  }

  public void saveState(long userId, MessageState state) {
    botStateMap.put(userId, state);
  }

  public MessageState getCurrentState(User user) {
    return Objects.nonNull(botStateMap.get(user.getId()))
        ? botStateMap.get(user.getId())
        : MessageState.WELCOME;
  }
}
