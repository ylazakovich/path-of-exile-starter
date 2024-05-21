package io.starter.pages.telegram;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import io.starter.annotations.PageUrl;
import io.starter.steps.models.Page;

@PageUrl("/k")
public class HomePage implements Page {

  private SelenideElement primaryBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));

  public SelenideElement getLoginBtnWithPhone() {
    return primaryBtn;
  }

  public void clickPrimaryBtn() {
    primaryBtn.click();
  }
}
