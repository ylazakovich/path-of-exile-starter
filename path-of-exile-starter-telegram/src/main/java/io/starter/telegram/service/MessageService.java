package io.starter.telegram.service;

import java.util.List;

import io.starter.aggregator.model.Skill;
import io.starter.telegram.dao.AnalyzedSkillsDAO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageService {

  private final AnalyzedSkillsDAO analyzedSkillsDAO;

  public MessageService(AnalyzedSkillsDAO analyzedSkillsDAO) {
    this.analyzedSkillsDAO = analyzedSkillsDAO;
  }

  @SneakyThrows
  public SendMessage messageWithReadySkillsForTrade(Message message) {
    final List<Skill> skills = analyzedSkillsDAO.findAll();
    // TODO: simplify output message
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(skills.toString());
    return sendMessage;
  }
}
