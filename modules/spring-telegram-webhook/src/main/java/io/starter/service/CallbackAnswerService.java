package io.starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.constants.CurrencyDisplay;
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
import io.starter.model.aggregator.VendorRecipeDiagnostic;
import io.starter.model.aggregator.VendorRecipeItemDiagnostic;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CallbackAnswerService {

  private static final int TABLE_WIDTH = 52;
  private static final int SKILLS_PER_PAGE = 10;
  private static final int VENDOR_RECIPES_PER_PAGE = 1;
  private static final int VENDOR_INGREDIENT_ROWS = 5;
  private static final int BOX_LINE_CONTENT_WIDTH = TABLE_WIDTH - 4;
  private static final Duration VENDOR_DIAGNOSTICS_REQUEST_TIMEOUT = Duration.ofSeconds(2);
  private static final Duration VENDOR_DIAGNOSTICS_CACHE_TTL = Duration.ofMinutes(2);

  private final DataAccessService dataAccessService;
  private final UserDao userDao;
  private final SettingsService settingsService;
  private final A8rService a8rService;
  private final ConcurrentMap<String, CachedVendorDiagnostics> vendorDiagnosticsCache = new ConcurrentHashMap<>();
  private final ConcurrentMap<String, Boolean> vendorDiagnosticsRefreshInFlight = new ConcurrentHashMap<>();

  public EditMessageText onClickSetting(CallbackQuery callbackQuery) {
    CallbackState callbackState = CallbackState.byData(callbackQuery.getData());
    League league = League.byCallbackState(callbackState);
    if (league != null) {
      userDao.saveLeague(callbackQuery.getFrom(), league);
    }
    CurrencyDisplay currencyDisplay = CurrencyDisplay.byCallbackState(callbackState);
    if (currencyDisplay != null) {
      userDao.saveCurrency(callbackQuery.getFrom(), currencyDisplay);
    }
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
    CurrencyDisplay currencyDisplay = resolveCurrency(userDao.readCurrency(from));
    Double divineRate = readDivineRate(leagueEntity);
    CallbackState callbackState = CallbackState.byData(callbackQuery.getData());
    int page = checkAndSyncPage(userDao.readSkillPage(from), skills.size(), SKILLS_PER_PAGE);
    int totalPages = calculateTotalPages(skills.size(), SKILLS_PER_PAGE);
    String inlineMessage = StringUtils.EMPTY;
    if (callbackState == CallbackState.SKILLS || callbackState == CallbackState.REFRESH_SKILLS) {
      inlineMessage = toPaginatedSkillMessage(page, skills, leagueEntity, currencyDisplay, divineRate);
    }
    if (callbackState == CallbackState.SKILLS_NEXT) {
      page = checkAndSyncPage(++page, skills.size(), SKILLS_PER_PAGE);
      inlineMessage = toPaginatedSkillMessage(page, skills, leagueEntity, currencyDisplay, divineRate);
    }
    if (callbackState == CallbackState.SKILLS_PREVIOUS) {
      page = checkAndSyncPage(--page, skills.size(), SKILLS_PER_PAGE);
      inlineMessage = toPaginatedSkillMessage(page, skills, leagueEntity, currencyDisplay, divineRate);
    }
    userDao.saveSkillPage(from, page);
    InlineKeyboardMarkup keyboard = onClickSkills(page, totalPages);
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  public EditMessageText onClickVendorRecipes(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    CurrencyDisplay currencyDisplay = resolveCurrency(userDao.readCurrency(from));
    Double divineRate = readDivineRate(leagueEntity);
    List<VendorRecipeEntity> recipes = dataAccessService.findAllVendorRecipes(leagueEntity);
    CallbackState callbackState = CallbackState.byData(callbackQuery.getData());
    int page = userDao.readRecipePage(from);
    int totalPages = calculateTotalPages(recipes.size(), VENDOR_RECIPES_PER_PAGE);
    String inlineMessage;
    if (recipes.isEmpty()) {
      inlineMessage = "Vendor recipes are not available for league '%s'.".formatted(leagueEntity.getName());
      page = 1;
      totalPages = 1;
    } else {
      if (callbackState == CallbackState.VENDOR_RECIPES_NEXT) {
        page = checkAndSyncPage(++page, recipes.size(), VENDOR_RECIPES_PER_PAGE);
      } else if (callbackState == CallbackState.VENDOR_RECIPES_PREVIOUS) {
        page = checkAndSyncPage(--page, recipes.size(), VENDOR_RECIPES_PER_PAGE);
      } else {
        page = checkAndSyncPage(page, recipes.size(), VENDOR_RECIPES_PER_PAGE);
      }
      VendorRecipeEntity selectedRecipe = recipes.get(page - 1);
      VendorRecipeDiagnostic recipeDiagnostic = findVendorRecipeDiagnostic(leagueEntity.getName(), selectedRecipe.getName());
      inlineMessage = toDetailedVendorRecipeMessage(
          page,
          totalPages,
          leagueEntity,
          selectedRecipe,
          recipeDiagnostic,
          currencyDisplay,
          divineRate
      );
    }
    userDao.saveRecipePage(from, page);
    InlineKeyboardMarkup keyboard = onClickVendorRecipes(page, totalPages, recipes.isEmpty());
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  public EditMessageText onClickAnimaStone(CallbackQuery callbackQuery) {
    User from = callbackQuery.getFrom();
    LeagueEntity leagueEntity = userDao.readLeague(from);
    CurrencyDisplay currencyDisplay = resolveCurrency(userDao.readCurrency(from));
    Double divineRate = readDivineRate(leagueEntity);
    Optional<VendorRecipeEntity> recipe = dataAccessService
        .findVendorRecipeByNameAndLeague(Constants.Recipes.ANIMA_STONE, leagueEntity);
    String inlineMessage = toAnimaStoneMessage(leagueEntity, recipe, currencyDisplay, divineRate);
    InlineKeyboardMarkup keyboard = onClickAnimaStone();
    return EditMessageGenerator.generate(callbackQuery.getMessage(), inlineMessage, keyboard);
  }

  private InlineKeyboardMarkup onClickSkills(int page, int totalPages) {
    InlineKeyboardButton linkToGuide =
        InlineKeyboardButtonGenerator.generate("Link to guide", CallbackState.NO_CMD.value);
    linkToGuide.setUrl(Constants.Start.SKILLS_GUIDE_LINK);
    List<InlineKeyboardButton> headerButtons = List.of(linkToGuide);
    List<InlineKeyboardButton> bodyButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.LEFT.value, CallbackState.SKILLS_PREVIOUS.value),
        InlineKeyboardButtonGenerator.generate("%d/%d".formatted(page, totalPages), CallbackState.CURRENT.value),
        InlineKeyboardButtonGenerator.generate(Emoji.RIGHT.value, CallbackState.SKILLS_NEXT.value)
    );
    List<InlineKeyboardButton> footerButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.REPEAT.value, CallbackState.REFRESH_SKILLS.value)
    );
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(headerButtons, bodyButtons, footerButtons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private String toPaginatedSkillMessage(int page,
                                         List<Skill> skills,
                                         LeagueEntity league,
                                         CurrencyDisplay currencyDisplay,
                                         Double divineRate) {
    int start = (page - 1) * SKILLS_PER_PAGE;
    int end = Math.min(start + SKILLS_PER_PAGE, skills.size());
    final StringBuilder builder = new StringBuilder();
    builder.append("```").append("\n");
    builder.append(formatTopBorder(league.getName())).append("\n");
    for (int i = start; i < end; i++) {
      String displayValue = formatDisplayedValue(skills.get(i).getChaosEquivalentProfit(), currencyDisplay, divineRate);
      builder.append(formatVendorLine(formatAlignedContentLine(skills.get(i).getName(), displayValue))).append("\n");
    }
    builder.append("└").append("─".repeat(TABLE_WIDTH - 2)).append("┘").append("\n");
    builder.append("```");
    return builder.toString();
  }

  private String toDetailedVendorRecipeMessage(int page,
                                               int totalPages,
                                               LeagueEntity league,
                                               VendorRecipeEntity recipe,
                                               VendorRecipeDiagnostic diagnostic,
                                               CurrencyDisplay currencyDisplay,
                                               Double divineRate) {
    List<String> ingredientLines = new ArrayList<>();
    boolean hasDetailedIngredients = diagnostic != null
        && diagnostic.ingredients() != null
        && !diagnostic.ingredients().isEmpty();

    if (hasDetailedIngredients) {
      for (VendorRecipeItemDiagnostic ingredient : diagnostic.ingredients()) {
        if (ingredient == null) {
          continue;
        }
        int quantity = ingredient.quantity() == null || ingredient.quantity() < 1 ? 1 : ingredient.quantity();
        Double unitPrice = ingredient.selectedChaosEquivalent();
        String ingredientLabel = "%s x%d".formatted(sanitize(ingredient.name()), quantity);
        if (unitPrice == null) {
          ingredientLines.add(formatAlignedContentLine(ingredientLabel, "n/a"));
          continue;
        }
        double ingredientTotal = unitPrice * quantity;
        ingredientLines.add(formatAlignedContentLine(
            ingredientLabel,
            formatDisplayedValue(ingredientTotal, currencyDisplay, divineRate)
        ));
      }
    } else {
      ingredientLines.add("n/a");
    }

    if (ingredientLines.size() > VENDOR_INGREDIENT_ROWS) {
      int hiddenIngredients = ingredientLines.size() - (VENDOR_INGREDIENT_ROWS - 1);
      ingredientLines = new ArrayList<>(ingredientLines.subList(0, VENDOR_INGREDIENT_ROWS - 1));
      ingredientLines.add("... +%d more".formatted(hiddenIngredients));
    }
    while (ingredientLines.size() < VENDOR_INGREDIENT_ROWS) {
      ingredientLines.add(StringUtils.EMPTY);
    }

    StringBuilder builder = new StringBuilder();
    String header = league.getName();
    builder.append("```").append("\n");
    builder.append(formatTopBorder(header)).append("\n");
    builder.append(formatVendorLine(formatAlignedContentLine(
        sanitize(recipe.getName()),
        formatDisplayedValue(recipe.getChaosEquivalentPrice(), currencyDisplay, divineRate)))).append("\n");
    builder.append(formatVendorLine(StringUtils.EMPTY)).append("\n");
    builder.append(formatSectionDivider("Ingredients")).append("\n");
    builder.append(formatVendorLine(StringUtils.EMPTY)).append("\n");
    ingredientLines.forEach(ingredient -> builder.append(formatVendorLine(ingredient)).append("\n"));
    builder.append(formatVendorLine(StringUtils.EMPTY)).append("\n");
    builder.append("└").append("─".repeat(TABLE_WIDTH - 2)).append("┘").append("\n");
    builder.append("```");
    return builder.toString();
  }

  private VendorRecipeDiagnostic findVendorRecipeDiagnostic(String leagueName, String recipeName) {
    refreshVendorRecipeDiagnosticsAsync(leagueName, recipeName);
    List<VendorRecipeDiagnostic> diagnostics = getCachedVendorRecipeDiagnostics(leagueName);
    VendorRecipeDiagnostic matchedDiagnostic = diagnostics.stream()
        .filter(diagnostic -> diagnostic != null && recipeName.equalsIgnoreCase(diagnostic.recipeName()))
        .findFirst()
        .orElse(null);
    if (matchedDiagnostic == null) {
      matchedDiagnostic = diagnostics.stream()
          .filter(diagnostic -> diagnostic != null
              && normalizeName(recipeName).equals(normalizeName(diagnostic.recipeName())))
          .findFirst()
          .orElse(null);
    }
    if (matchedDiagnostic == null) {
      log.warn(
          "Vendor diagnostics recipe is not matched: league='{}', recipe='{}', diagnosticsCount={}",
          leagueName,
          recipeName,
          diagnostics.size()
      );
    }
    return matchedDiagnostic;
  }

  private List<VendorRecipeDiagnostic> getCachedVendorRecipeDiagnostics(String leagueName) {
    String cacheKey = normalizeName(leagueName);
    CachedVendorDiagnostics cached = vendorDiagnosticsCache.get(cacheKey);
    if (cached != null) {
      return cached.diagnostics();
    }
    return List.of();
  }

  private void refreshVendorRecipeDiagnosticsAsync(String leagueName, String recipeName) {
    String cacheKey = normalizeName(leagueName);
    long now = System.currentTimeMillis();
    CachedVendorDiagnostics cached = vendorDiagnosticsCache.get(cacheKey);
    boolean isCacheFresh = cached != null && now - cached.loadedAtMillis() <= VENDOR_DIAGNOSTICS_CACHE_TTL.toMillis();
    if (isCacheFresh || vendorDiagnosticsRefreshInFlight.putIfAbsent(cacheKey, Boolean.TRUE) != null) {
      return;
    }

    a8rService.getVendorRecipeDiagnostics(leagueName)
        .timeout(VENDOR_DIAGNOSTICS_REQUEST_TIMEOUT)
        .onErrorResume(exception -> {
          log.warn(
              "Failed to refresh vendor diagnostics for league='{}', recipe='{}'.",
              leagueName,
              recipeName,
              exception
          );
          return Mono.just(List.of());
        })
        .doFinally(signalType -> vendorDiagnosticsRefreshInFlight.remove(cacheKey))
        .subscribe(diagnostics -> {
          List<VendorRecipeDiagnostic> safeDiagnostics = diagnostics == null ? List.of() : List.copyOf(diagnostics);
          vendorDiagnosticsCache.put(cacheKey, new CachedVendorDiagnostics(safeDiagnostics, System.currentTimeMillis()));
        });
  }

  private int checkAndSyncPage(int page, int itemsCount, int itemsPerPage) {
    int totalPages = calculateTotalPages(itemsCount, itemsPerPage);
    if (page < 1) {
      page = totalPages;
    }
    if (page > totalPages) {
      page = 1;
    }
    return page;
  }

  private int calculateTotalPages(int itemsCount, int itemsPerPage) {
    return Math.max((int) Math.ceil((double) itemsCount / itemsPerPage), 1);
  }

  private String sanitize(String value) {
    if (value == null) {
      return "n/a";
    }
    return value.replace("`", "'");
  }

  private String normalizeName(String value) {
    if (value == null) {
      return StringUtils.EMPTY;
    }
    return value.trim().replaceAll("\\s+", " ").toLowerCase();
  }

  private String formatDisplayedValue(double chaosValue, CurrencyDisplay currencyDisplay, Double divineRate) {
    return formatDisplayedValue(Double.valueOf(chaosValue), currencyDisplay, divineRate);
  }

  private String formatDisplayedValue(Double chaosValue, CurrencyDisplay currencyDisplay, Double divineRate) {
    if (chaosValue == null) {
      return "n/a";
    }
    if (currencyDisplay == CurrencyDisplay.DIVINE && divineRate != null && divineRate > 0) {
      return "%.2f div".formatted(chaosValue / divineRate);
    }
    return formatChaosValue(chaosValue);
  }

  private String formatChaosValueSigned(double value) {
    return "%+d c".formatted(Math.round(value));
  }

  private String formatVendorLine(String content) {
    String sanitized = content == null ? StringUtils.EMPTY : content;
    if (sanitized.length() > BOX_LINE_CONTENT_WIDTH) {
      sanitized = sanitized.substring(0, BOX_LINE_CONTENT_WIDTH - 3) + "...";
    }
    return String.format("│ %-" + BOX_LINE_CONTENT_WIDTH + "s │", sanitized);
  }

  private String formatTopBorder(String title) {
    String safeTitle = title == null ? StringUtils.EMPTY : title;
    int innerWidth = TABLE_WIDTH - 2;
    int maxTitleLength = innerWidth - 2;
    if (safeTitle.length() > maxTitleLength) {
      safeTitle = safeTitle.substring(0, maxTitleLength - 3) + "...";
    }
    int leftPad = (innerWidth - safeTitle.length() - 2) / 2;
    int rightPad = innerWidth - safeTitle.length() - 2 - leftPad;
    return "┌" + "─".repeat(leftPad) + " " + safeTitle + " " + "─".repeat(rightPad) + "┐";
  }

  private String toAnimaStoneMessage(LeagueEntity league,
                                     Optional<VendorRecipeEntity> recipe,
                                     CurrencyDisplay currencyDisplay,
                                     Double divineRate) {
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
        eminence.orElse(null),
        currencyDisplay,
        divineRate
    );
  }

  private String formatAnimaStoneMessage(String leagueName,
                                         Double profit,
                                         UniqueJewelEntity might,
                                         UniqueJewelEntity harmony,
                                         UniqueJewelEntity eminence,
                                         CurrencyDisplay currencyDisplay,
                                         Double divineRate) {
    StringBuilder builder = new StringBuilder();
    builder.append("```").append("\n");
    builder.append(formatTopBorder(leagueName)).append("\n");
    builder.append(formatVendorLine(formatAlignedContentLine(
        "Anima Stone profit",
        formatDisplayedValue(profit, currencyDisplay, divineRate)
    ))).append("\n");
    builder.append(formatSectionDivider("Ingredients")).append("\n");
    builder.append(formatVendorLine(formatAlignedContentLine(
        Constants.Recipes.PRIMORDIAL_MIGHT,
        formatDisplayedValue(might == null ? null : might.getChaosEquivalent(), currencyDisplay, divineRate)
    ))).append("\n");
    builder.append(formatVendorLine(formatAlignedContentLine(
        Constants.Recipes.PRIMORDIAL_HARMONY,
        formatDisplayedValue(harmony == null ? null : harmony.getChaosEquivalent(), currencyDisplay, divineRate)
    ))).append("\n");
    builder.append(formatVendorLine(formatAlignedContentLine(
        Constants.Recipes.PRIMORDIAL_EMINENCE,
        formatDisplayedValue(eminence == null ? null : eminence.getChaosEquivalent(), currencyDisplay, divineRate)
    ))).append("\n");
    builder.append("└").append("─".repeat(TABLE_WIDTH - 2)).append("┘").append("\n");
    builder.append("```");
    return builder.toString();
  }

  private String formatAlignedContentLine(String label, String value) {
    String safeLabel = label == null ? "n/a" : label;
    String safeValue = value == null ? "n/a" : value;
    int maxLabelWidth = 34;
    int valueWidth = 11;
    if (safeLabel.length() > maxLabelWidth) {
      safeLabel = safeLabel.substring(0, maxLabelWidth - 3) + "...";
    }
    return String.format("%-" + maxLabelWidth + "s : %" + valueWidth + "s", safeLabel, safeValue);
  }

  private String formatChaosValue(Double value) {
    if (value == null) {
      return "n/a";
    }
    return "%d c".formatted(Math.round(value));
  }

  private String formatSectionDivider(String title) {
    int innerWidth = TABLE_WIDTH - 2;
    int titleLength = title.length();
    int leftPad = (innerWidth - titleLength - 2) / 2;
    int rightPad = innerWidth - titleLength - 2 - leftPad;
    return "│" + "─".repeat(leftPad) + " " + title + " " + "─".repeat(rightPad) + "│";
  }

  private CurrencyDisplay resolveCurrency(CurrencyDisplay currencyDisplay) {
    return currencyDisplay == null ? CurrencyDisplay.CHAOS : currencyDisplay;
  }

  private Double readDivineRate(LeagueEntity leagueEntity) {
    if (leagueEntity == null) {
      return null;
    }
    Optional<Double> maybeRate = Optional.ofNullable(dataAccessService.findDivineOrbChaosRate(leagueEntity))
        .orElse(Optional.empty());
    return maybeRate.orElse(null);
  }

  private record CachedVendorDiagnostics(List<VendorRecipeDiagnostic> diagnostics, long loadedAtMillis) {
  }

  private InlineKeyboardMarkup onClickAnimaStone() {
    InlineKeyboardButton refresh = InlineKeyboardButtonGenerator
        .generate(Emoji.REPEAT.value, CallbackState.REFRESH_ANIMA_STONE.value);
    List<InlineKeyboardButton> row = List.of(refresh);
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(row);
    return InlineKeyboardGenerator.withRows(keyboard);
  }

  private InlineKeyboardMarkup onClickVendorRecipes(int page, int totalPages, boolean isEmpty) {
    List<InlineKeyboardButton> bodyButtons;
    if (isEmpty || totalPages <= 1) {
      bodyButtons = List.of(
          InlineKeyboardButtonGenerator.generate("%d/%d".formatted(page, totalPages), CallbackState.CURRENT.value)
      );
    } else {
      bodyButtons = List.of(
          InlineKeyboardButtonGenerator.generate(Emoji.LEFT.value, CallbackState.VENDOR_RECIPES_PREVIOUS.value),
          InlineKeyboardButtonGenerator.generate("%d/%d".formatted(page, totalPages), CallbackState.CURRENT.value),
          InlineKeyboardButtonGenerator.generate(Emoji.RIGHT.value, CallbackState.VENDOR_RECIPES_NEXT.value)
      );
    }
    List<InlineKeyboardButton> footerButtons = List.of(
        InlineKeyboardButtonGenerator.generate(Emoji.REPEAT.value, CallbackState.REFRESH_VENDOR_RECIPES.value)
    );
    List<InlineKeyboardRow> keyboard = InlineKeyboardRowGenerator.generate(bodyButtons, footerButtons);
    return InlineKeyboardGenerator.withRows(keyboard);
  }
}
