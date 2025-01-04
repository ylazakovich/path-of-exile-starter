package io.starter.tests.third_party;

import io.starter.BaseUiTest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

@Epic("Third party services")
public class HealthCheckTest extends BaseUiTest {

  @Feature("Telegram")
  @Test(description = "Web telegram should be available for login")
  void testGetTelegramWebApp() {
    telegramNavigationSteps.openLoginPage()
        .verify()
        .verifyLoginDisplayed();
  }

  @Feature("poe.ninja")
  @Test(enabled = false, description = "Web telegram should be available for login")
  void testGetPoeNinjaWebApp() {
    // TODO: remote run catches webdriver render exception
    poeNinjaNavigationSteps.openHomePage()
        .verify()
        .verifySearchDisplayed();
  }
}
