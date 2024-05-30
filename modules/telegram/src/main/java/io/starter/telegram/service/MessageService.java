package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.dao.AnalyzedSkillsDAO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class MessageService {

  private final AnalyzedSkillsDAO analyzedSkillsDAO;

  public MessageService(AnalyzedSkillsDAO analyzedSkillsDAO) {
    this.analyzedSkillsDAO = analyzedSkillsDAO;
  }

  @SneakyThrows
  public SendMessage messageWithReadySkillsForTrade(CallbackQuery query) {
    final List<Skill> skills = analyzedSkillsDAO.findAll();
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(query.getMessage().getChatId());
    sendMessage.setText(initBuilder(skills).toString());
    return sendMessage;
  }

  private StringBuilder initBuilder(List<Skill> skills) {
    final StringBuilder builder = new StringBuilder();
    skills.forEach(skill -> builder
        .append(skill.getName())
        .append(" : ")
        .append(Math.round(skill.getProfit()))
        .append("\n"));
    return builder;
  }
}
