package io.starter.integrations;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.starter.BaseUITest;
import org.testng.annotations.Test;

public class LoginTest extends BaseUITest {

  @Test()
  void testLogin() {
    Selenide.open("https://web.telegram.org/k");
//    SelenideElement byPhoneBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));
//    byPhoneBtn.click();
//    SelenideElement phoneField =
//        $(Selectors.byCssSelector("div[class$=input-field-phone] > div[class=input-field-input]"));
//    phoneField.setValue("");
//    SelenideElement nextBtn = $(Selectors.byXpath("//button[contains(@class, 'btn-primary')]/span[text()='Next']"));
//    nextBtn.click();
//    SelenideElement code = $(Selectors.byCssSelector("input[type=tel]"));
//    code.setValue("");

    // TODO: Part of the other test
//    SelenideElement searchInput = $(Selectors.byCssSelector("div[class=input-search]"));
//    searchInput.setValue("@poe_consultant_bot");
//    SelenideElement result =
//        $(Selectors.byXpath("//div[contains(@class, 'search-group')]/ul/a/div/div[text()='@poe_consultant_bot']"));
//    result.click();
    // TODO: Need to make a controller with assigning code for auth then continue test
  }
}
