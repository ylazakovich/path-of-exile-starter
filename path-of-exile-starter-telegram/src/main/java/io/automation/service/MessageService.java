package io.automation.service;

import java.util.ArrayList;
import java.util.List;

import io.automation.aggregator.model.Skill;
import io.automation.dao.AnalyzedSkillsDAO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageService {

  private final AggregatorService aggregatorService;
  private final AnalyzedSkillsDAO analyzedSkillsDAO;

  public MessageService(AggregatorService aggregatorService,
                        AnalyzedSkillsDAO analyzedSkillsDAO) {
    this.aggregatorService = aggregatorService;
    this.analyzedSkillsDAO = analyzedSkillsDAO;
  }

  @SneakyThrows
  public SendMessage getAnalyzedSkills(Message message) {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::updateAll);
    final List<Skill> skills = analyzedSkillsDAO.findAll();
    // TODO: need to add AWAITILITY + simplify out put message
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(message.getChatId());
    sendMessage.setText(skills.toString());
    return sendMessage;
  }
}
