package io.automation.service;

import java.util.ArrayList;
import java.util.List;

import io.automation.aggregator.model.Skill;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageService {

  private final AggregatorService aggregatorService;

  public MessageService(AggregatorService aggregatorService) {
    this.aggregatorService = aggregatorService;
  }

  public SendMessage getAnalyzedSkills(Message message) {
    List<Skill> skills = new ArrayList<>();
    // TODO: Subscribe skips - I don't know why need to investigate
    aggregatorService.getAnalyzedSkills().log()
        .subscribe(data -> skills.addAll(data.getBody()));
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(skills.toString());
    return sendMessage;
  }
}
