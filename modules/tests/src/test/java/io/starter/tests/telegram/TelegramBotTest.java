package io.starter.tests.telegram;

import io.starter.BaseUITest;
import io.starter.config.AppConfig;
import io.starter.steps.telegram.HomePageSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TelegramBotTest extends BaseUITest {

  private HomePageSteps steps;

  @BeforeClass
  void session() {
    steps = telegramNavigationSteps
        .openLoginPage().loginViaPhone(AppConfig.ACCOUNT_PHONE)
        .inputVerificationCode(regressionServiceSteps.getValidationCode());
  }

  @Test(enabled = false)
  void testUserOnSuccessfulLogin() {
    steps.verify().verifySearchDisplayed();
  }

  @Test(enabled = false)
  void testUserOnStart() {
    // TODO: Part of the other test
//    SelenideElement searchInput = $(Selectors.byCssSelector("div[class=input-search]"));
//    searchInput.setValue("@poe_consultant_bot");
//    SelenideElement result =
//    $(Selectors.byXpath("//div[contains(@class, 'search-group')]/ul/a/div/div[text()='@poe_consultant_bot']"));
//    result.click();
  }

}
