package io.starter.ui.telegram;

import io.starter.steps.models.Verification;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

public class LoginPageVerification extends Verification<LoginPageSteps, LoginPage> {

  public LoginPageVerification(LoginPageSteps steps, LoginPage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public LoginPageVerification verifyLoginDisplayed() {
    page.getLoginBtn().shouldBe(Condition.visible);
    return this;
  }
}
