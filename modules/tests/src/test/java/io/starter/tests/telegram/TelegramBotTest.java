package io.starter.tests.telegram;

import io.starter.BaseUITest;
import io.starter.config.AppConfig;
import org.testng.annotations.Test;

public class TelegramBotTest extends BaseUITest {

  // TODO: I think that it should be private method which should run only 1 time
  @Test(enabled = true)
  void testOnAssigningVerificationCode() {
    String code = regressionServiceSteps.getValidationCode();
    telegramNavigationSteps
        .openLoginPage().loginViaPhone(AppConfig.ACCOUNT_PHONE)
        .inputVerificationCode(code);
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

  @Test(enabled = false)
  void testUserOnWelcomeMessage() {
  }

}
