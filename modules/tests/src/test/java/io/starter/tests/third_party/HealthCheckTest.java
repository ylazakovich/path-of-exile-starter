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
  @Test(description = "Web telegram should be available for login")
  void testGetPoeNinjaWebApp() {
    poeNinjaNavigationSteps.openHomePage()
        .verify()
        .verifySearchDisplayed();
  }
}
