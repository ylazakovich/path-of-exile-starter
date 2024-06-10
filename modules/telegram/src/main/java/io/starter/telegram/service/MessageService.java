package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.dao.AnalyzedSkillsDao;
import io.starter.telegram.model.aggregator.Skill;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class MessageService {

  private final AnalyzedSkillsDao analyzedSkillsDAO;
  private final MenuService menuService;

  public MessageService(AnalyzedSkillsDao analyzedSkillsDAO,
                        MenuService menuService) {
    this.analyzedSkillsDAO = analyzedSkillsDAO;
    this.menuService = menuService;
  }

  public EditMessageText messageWithReadySkillsForTrade(CallbackQuery callback) {
    final List<Skill> skills = analyzedSkillsDAO.findAll();
    InlineKeyboardMarkup markup = menuService.keyboardWithRefresh();
    return menuService.generateEditMessage(callback.getMessage(), initBuilder(skills).toString(), markup);
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
