package io.starter.tests.telegram;

import io.starter.BaseUITest;
import io.starter.annotations.Nightly;
import io.starter.config.AppConfig;
import io.starter.steps.telegram.HomePageSteps;
import io.starter.utils.Browser;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TelegramBotTest extends BaseUITest {

  private HomePageSteps steps;

  @BeforeClass
  void session() {
    steps = telegramNavigationSteps
        .openLoginPage().loginViaPhone(AppConfig.ACCOUNT_PHONE)
        .inputVerificationCode(regressionServiceSteps.getCode());
    regressionServiceSteps.expireCode();
  }

  @Test()
  @Nightly
  void testUserOnSuccessfulLogin() {
    steps.verify().verifySearchDisplayed();
  }

  @Nightly(isEnabled = true)
  @Test()
  void testUserOnWelcomeMessage() {
    steps.verify().verifySearchDisplayed();
  }

  @AfterClass
  void tearDown() {
    Browser.closeWebDriver();
  }
}
