package io.starter.pages.telegram;

import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import io.starter.annotations.PageUrl;
import io.starter.steps.models.Page;

@PageUrl("/k")
public class LoginPage implements Page {

  private SelenideElement primaryBtn = $(byCssSelector("button[class^='btn-primary']"));
  private SelenideElement phoneInput = $(byCssSelector("div[class$=input-field-phone] > div[class=input-field-input]"));
  private SelenideElement nextBtn = $(byXpath("//button[contains(@class, 'btn-primary')]/span[text()='Next']"));
  private SelenideElement codeInput = $(byCssSelector("input[type=tel]"));

  public SelenideElement getLoginBtn() {
    return primaryBtn;
  }

  public void clickPrimaryBtn() {
    primaryBtn.click();
  }

  public void enterPhone(String phone) {
    phoneInput.setValue(phone);
  }

  public void clickNextBtn() {
    nextBtn.click(ClickOptions.usingJavaScript());
  }

  public void inputCode(String code) {
    codeInput.setValue(code);
  }
}
