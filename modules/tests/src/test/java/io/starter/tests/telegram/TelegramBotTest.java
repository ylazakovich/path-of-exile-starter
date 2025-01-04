package io.starter.tests.telegram;

import io.starter.BaseUiTest;
import io.starter.config.AppConfig;
import io.starter.ui.telegram.steps.HomePageSteps;
import io.starter.utils.Browser;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Service Telegram Bot")
public class TelegramBotTest extends BaseUiTest {

  private HomePageSteps steps;

  @BeforeClass
  void session() {
    steps = telegramNavigationSteps
        .openLoginPage().loginViaPhone(AppConfig.ACCOUNT_PHONE)
        .inputVerificationCode(regressionServiceSteps.getCode());
    regressionServiceSteps.expireCode();
  }

  @Feature("'/start'")
  @Test(enabled = false, description = "User should be able to see command 'START'")
  void testUserOnWelcomeMessage() {
    String user = AppConfig.BOT_USERNAME;
    steps.search(user)
        .selectChat(0)
        .verify()
        .verifyOpenedChat();
  }

  @AfterClass
  void tearDown() {
    Browser.closeWebDriver();
  }
}
