package io.starter.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.constants.Emoji;
import io.starter.constants.League;
import io.starter.dao.UserDao;
import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.generator.messages.EditMessageGenerator;
import io.starter.generator.replykeyboard.InlineKeyboardGenerator;
import io.starter.generator.replykeyboard.buttons.InlineKeyboardButtonGenerator;
import io.starter.generator.replykeyboard.rows.InlineKeyboardRowGenerator;
import io.starter.model.aggregator.Skill;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

@Service
@AllArgsConstructor
public class CallbackAnswerService {

  private final DataAccessService dataAccessService;
  private final UserDao userDao;
  private final SettingsService settingsService;

  public EditMessageText onClickSetting(CallbackQuery callbackQuery) {
    League league = League.byCallbackState(CallbackState.byData(callbackQuery.getData()));
    userDao.saveLeague(callbackQuery.getFrom(), Objects.requireNonNull(league));
    InlineKeyboardMarkup inlineKeyboard = settingsService.generateKeyboard();
    return EditMessageGenerator.generate(
        callbackQuery.getMessage(),
        settingsService.generateInlineMessage(callbackQuery.getFrom()),
        inlineKeyboard);
  }

  public EditMessageText onClickSkills(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    List<Skill> skills = dataAccessService.findAllSkills(leagueEntity);
    CallbackState callbackState = CallbackState.byData(callbackQuery.getData());
    int page = userDao.readSkillPage(from);
    String inlineMessage = StringUtils.EMPTY;
    if (callbackState == CallbackState.SKILLS || callbackState == CallbackState.REFRESH_SKILLS) {
      inlineMessage = toPaginatedMessage(page, skills, leagueEntity);
    }
    if (callbackState == CallbackState.SKILLS_NEXT) {
      page = checkAndSyncPage(++page, skills);
      inlineMessage = toPaginatedMessage(page, skills, leagueEntity);
    }
    if (callbackState == CallbackState.SKILLS_PREVIOUS) {
      page = checkAndSyncPage(--page, skills);
      inlineMessage = toPaginatedMessage(page, skills, leagueEntity);
    }
    userDao.saveSkillPage(from, page);
    InlineKeyboardMarkup keyboard = onClickSkills(page);
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  public EditMessageText onClickVendorRecipes(CallbackQuery callbackQuery) {
    InlineKeyboardMarkup keyboard = onClickVendorRecipes();
    String inlineMessage = "Select a vendor recipe:";
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  public EditMessageText onClickAnimaStone(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    Optional<VendorRecipeEntity> recipe = dataAccessService
        .findVendorRecipeByNameAndLeague(Constants.Recipes.ANIMA_STONE, leagueEntity);
    String inlineMessage = toAnimaStoneMessage(leagueEntity, recipe);
    InlineKeyboardMarkup keyboard = onClickAnimaStone();
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup onClickSkills(int page) {
    InlineKeyboardButton linkToGuide =
        InlineKeyboardButtonGenerator.generate("Link to guide", CallbackState.NO_CMD.value);
    linkToGuide.setUrl(Constants.Start.SKILLS_GUIDE_LINK);
    List<InlineKeyboardButton> headerButtons = List.of(linkToGuide);
    List<InlineKeyboardButton> bodyButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.LEFT.value, CallbackState.SKILLS_PREVIOUS.value),
        InlineKeyboardButtonGenerator.generate(String.valueOf(page), CallbackState.CURRENT.value),
        InlineKeyboardButtonGenerator.generate(Emoji.RIGHT.value, CallbackState.SKILLS_NEXT.value)
    );
    List<InlineKeyboardButton> footerButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.REPEAT.value, CallbackState.REFRESH_SKILLS.value)
    );
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(headerButtons, bodyButtons, footerButtons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private String toPaginatedMessage(int page, List<Skill> skills, LeagueEntity league) {
    final int itemsPerPage = 10;
    int start = (page - 1) * itemsPerPage;
    int end = Math.min(start + itemsPerPage, skills.size());
    final StringBuilder builder = new StringBuilder();
    String leagueName = league.getName();
    int totalWidth = 52;
    int nameLength = leagueName.length();
    int innerWidth = totalWidth - 2;
    int leftPad = (innerWidth - nameLength - 2) / 2;
    int rightPad = innerWidth - nameLength - 2 - leftPad;
    String topBorder = "┌" + "─".repeat(leftPad) + " " + leagueName + " " + "─".repeat(rightPad) + "┐";
    builder.append("```").append("\n");
    builder.append(topBorder).append("\n");
    for (int i = start; i < end; i++) {
      long chaosEquivalentProfit = Math.round(skills.get(i).getChaosEquivalentProfit());
      String line = String.format("│ %-38s : %5d c │", skills.get(i).getName(), chaosEquivalentProfit);
      builder.append(line).append("\n");
    }
    builder.append("└").append("─".repeat(totalWidth - 2)).append("┘").append("\n");
    builder.append("```");
    return builder.toString();
  }

  private int checkAndSyncPage(int page, List<Skill> skills) {
    final int itemsPerPage = 10;
    int totalPages = (int) Math.ceil((double) skills.size() / itemsPerPage);
    if (page < 1) {
      page = totalPages;
    }
    if (page > totalPages) {
      page = 1;
    }
    return page;
  }

  private String toAnimaStoneMessage(LeagueEntity league, Optional<VendorRecipeEntity> recipe) {
    if (league == null) {
      return "Anima Stone data is not available: league is not selected.";
    }
    if (recipe.isEmpty()) {
      return "Anima Stone data is not available for league '%s'.".formatted(league.getName());
    }
    Optional<UniqueJewelEntity> might = dataAccessService
        .findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_MIGHT, league);
    Optional<UniqueJewelEntity> harmony = dataAccessService
        .findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_HARMONY, league);
    Optional<UniqueJewelEntity> eminence = dataAccessService
        .findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_EMINENCE, league);
    return formatAnimaStoneMessage(
        league.getName(),
        recipe.get().getChaosEquivalentProfit(),
        might.orElse(null),
        harmony.orElse(null),
        eminence.orElse(null)
    );
  }

  private String formatAnimaStoneMessage(String leagueName,
                                         Double profit,
                                         UniqueJewelEntity might,
                                         UniqueJewelEntity harmony,
                                         UniqueJewelEntity eminence) {
    final int totalWidth = 52;
    int nameLength = leagueName.length();
    int innerWidth = totalWidth - 2;
    int leftPad = (innerWidth - nameLength - 2) / 2;
    int rightPad = innerWidth - nameLength - 2 - leftPad;
    String topBorder = "┌" + "─".repeat(leftPad) + " " + leagueName + " " + "─".repeat(rightPad) + "┐";
    StringBuilder builder = new StringBuilder();
    builder.append("```").append("\n");
    builder.append(topBorder).append("\n");
    builder.append(formatAnimaStoneLine("Anima Stone profit", formatChaosValue(profit))).append("\n");
    builder.append(formatSectionDivider("Ingredients")).append("\n");
    builder.append(formatAnimaStoneLine(Constants.Recipes.PRIMORDIAL_MIGHT, formatChaosValue(might))).append("\n");
    builder.append(formatAnimaStoneLine(Constants.Recipes.PRIMORDIAL_HARMONY, formatChaosValue(harmony))).append("\n");
    builder.append(formatAnimaStoneLine(Constants.Recipes.PRIMORDIAL_EMINENCE, formatChaosValue(eminence)))
        .append("\n");
    builder.append("└").append("─".repeat(totalWidth - 2)).append("┘").append("\n");
    builder.append("```");
    return builder.toString();
  }

  private String formatAnimaStoneLine(String label, String value) {
    return String.format("│ %-38s : %7s │", label, value);
  }

  private String formatChaosValue(UniqueJewelEntity jewel) {
    if (jewel == null || jewel.getChaosEquivalent() == null) {
      return "n/a";
    }
    return "%d c".formatted(Math.round(jewel.getChaosEquivalent()));
  }

  private String formatChaosValue(Double value) {
    if (value == null) {
      return "n/a";
    }
    return "%d c".formatted(Math.round(value));
  }

  private String formatSectionDivider(String title) {
    final int totalWidth = 52;
    int innerWidth = totalWidth - 2;
    int titleLength = title.length();
    int leftPad = (innerWidth - titleLength - 2) / 2;
    int rightPad = innerWidth - titleLength - 2 - leftPad;
    return "│" + "─".repeat(leftPad) + " " + title + " " + "─".repeat(rightPad) + "│";
  }

  private InlineKeyboardMarkup onClickAnimaStone() {
    InlineKeyboardButton refresh = InlineKeyboardButtonGenerator
        .generate(Emoji.REPEAT.value, CallbackState.REFRESH_ANIMA_STONE.value);
    List<InlineKeyboardButton> row = List.of(refresh);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(row);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private InlineKeyboardMarkup onClickVendorRecipes() {
    InlineKeyboardButton animaStone = InlineKeyboardButtonGenerator
        .generate(Constants.Recipes.ANIMA_STONE, CallbackState.ANIMA_STONE.value);
    List<InlineKeyboardButton> row = List.of(animaStone);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(row);
    return InlineKeyboardGenerator.withRows(keyboard);
  }
}
