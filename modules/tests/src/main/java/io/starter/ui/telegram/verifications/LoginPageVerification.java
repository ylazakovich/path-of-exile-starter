package io.starter.ui.telegram.verifications;

import io.starter.steps.models.Verification;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import io.starter.ui.telegram.pages.LoginPage;
import io.starter.ui.telegram.steps.LoginPageSteps;

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
