package io.starter.tests.units.handler.callback.recipes;

import io.starter.cache.state.CallbackState;
import io.starter.constants.Constants;
import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.handler.UpdateHandler;
import io.starter.model.telegram.TelegramFacade;
import io.starter.tests.units.handler.callback.BaseCallbackTest;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class InteractionsTest extends BaseCallbackTest {

  @Test(description = "Bot should react on clicking button 'Anima Stone'")
  void testUserInteractionInAnimaStoneMenu() {
    LeagueEntity leagueEntity = mock(LeagueEntity.class);
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String callbackQueryId = String.valueOf(faker.number().positive());
    VendorRecipeEntity recipe = new VendorRecipeEntity(Constants.Recipes.ANIMA_STONE, 120.0, 55.0);
    UniqueJewelEntity might = new UniqueJewelEntity(Constants.Recipes.PRIMORDIAL_MIGHT, 20.0);
    UniqueJewelEntity harmony = new UniqueJewelEntity(Constants.Recipes.PRIMORDIAL_HARMONY, 30.0);
    UniqueJewelEntity eminence = new UniqueJewelEntity(Constants.Recipes.PRIMORDIAL_EMINENCE, 15.0);

    when(leagueEntity.getName()).thenReturn("Keepers");
    when(callbackQuery.getData()).thenReturn(CallbackState.ANIMA_STONE.value);
    when(callbackQuery.getId()).thenReturn(callbackQueryId);
    when(userDao.readLeague(user)).thenReturn(leagueEntity);
    when(dataAccessService.findVendorRecipeByNameAndLeague(Constants.Recipes.ANIMA_STONE, leagueEntity))
        .thenReturn(java.util.Optional.of(recipe));
    when(dataAccessService.findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_MIGHT, leagueEntity))
        .thenReturn(java.util.Optional.of(might));
    when(dataAccessService.findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_HARMONY, leagueEntity))
        .thenReturn(java.util.Optional.of(harmony));
    when(dataAccessService.findUniqueJewelByNameAndLeague(Constants.Recipes.PRIMORDIAL_EMINENCE, leagueEntity))
        .thenReturn(java.util.Optional.of(eminence));
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    EditMessageText expected = callbackAnswerService.onClickAnimaStone(callbackQuery);
    EditMessageText actual = (EditMessageText) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(EditMessageText.PATH);
    assertThat(actual.getText()).contains("Anima Stone profit");
    assertThat(actual.getText()).contains(Constants.Recipes.PRIMORDIAL_MIGHT);
    assertThat(actual.getText()).contains(Constants.Recipes.PRIMORDIAL_HARMONY);
    assertThat(actual.getText()).contains(Constants.Recipes.PRIMORDIAL_EMINENCE);
    assertThat(actual).isEqualTo(expected);
  }

  @Test(description = "Bot should react on clicking button 'Vendor Recipes'")
  void testUserInteractionInVendorRecipesMenu() {
    UpdateHandler handler = spy(new UpdateHandler(messageAnswerService, callbackAnswerService, userDao));
    TelegramFacade bot = spy(new TelegramFacade(handler, callbackCache, messageCache));
    String callbackQueryId = String.valueOf(faker.number().positive());

    when(callbackQuery.getData()).thenReturn(CallbackState.VENDOR_RECIPES.value);
    when(callbackQuery.getId()).thenReturn(callbackQueryId);
    BotApiMethod<?> botApiMethod = bot.handleOnUpdate(update);

    EditMessageText expected = callbackAnswerService.onClickVendorRecipes(callbackQuery);
    EditMessageText actual = (EditMessageText) botApiMethod;
    assertThat(botApiMethod.getMethod()).isEqualTo(EditMessageText.PATH);
    assertThat(actual.getText()).contains("Select a vendor recipe");
    assertThat(actual).isEqualTo(expected);
  }
}
