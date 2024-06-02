package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.dao.AnalyzedSkillsDAO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class MessageService {

  private final AnalyzedSkillsDAO analyzedSkillsDAO;
  private final MenuService menuService;

  public MessageService(AnalyzedSkillsDAO analyzedSkillsDAO,
                        MenuService menuService) {
    this.analyzedSkillsDAO = analyzedSkillsDAO;
    this.menuService = menuService;
  }

  @SneakyThrows
  public EditMessageText messageWithReadySkillsForTrade(CallbackQuery callback) {
    final List<Skill> skills = analyzedSkillsDAO.findAll();
    return menuService.generateEditMessage(callback.getMessage(), initBuilder(skills).toString());
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
