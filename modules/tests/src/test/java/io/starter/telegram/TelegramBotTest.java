package io.starter.telegram;

import io.starter.BaseUITest;
import org.testng.annotations.Test;

public class TelegramBotTest extends BaseUITest {

  // TODO: I think that it should be private method which should run only 1 time
  @Test(enabled = false)
  void testOnAssigningVerificationCode() {
//    SelenideElement byPhoneBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));
//    byPhoneBtn.click();
//    SelenideElement phoneField =
//    $(Selectors.byCssSelector("div[class$=input-field-phone] > div[class=input-field-input]"));
//    phoneField.setValue("");
//    SelenideElement nextBtn = $(Selectors.byXpath("//button[contains(@class, 'btn-primary')]/span[text()='Next']"));
//    nextBtn.click();
//    SelenideElement code = $(Selectors.byCssSelector("input[type=tel]"));
//    code.setValue("");
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
