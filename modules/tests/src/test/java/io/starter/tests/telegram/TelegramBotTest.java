package io.starter.tests.telegram;

import java.time.Duration;

import io.starter.BaseUITest;
import io.starter.config.AppConfig;
import io.starter.utils.Waiter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TelegramBotTest extends BaseUITest {

  // TODO: I think that it should be private method which should run only 1 time
  @BeforeClass
  void session() {
    // TODO: Can be make a call over Supplier
    Waiter.awaitCondition(() -> !regressionServiceSteps.getValidationCode().equals("0"),
        "No verification code. Admin can provide it",
        Duration.ofSeconds(30),
        Duration.ofMillis(500));
    String code = regressionServiceSteps.getValidationCode();
    telegramNavigationSteps
        .openLoginPage().loginViaPhone(AppConfig.ACCOUNT_PHONE)
        .inputVerificationCode(code);
  }

  @Test(enabled = false)
  void testOnAssigningVerificationCode() {
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
