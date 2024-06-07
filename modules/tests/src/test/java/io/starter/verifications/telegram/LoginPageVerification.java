package io.starter.verifications.telegram;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import io.starter.pages.telegram.LoginPage;
import io.starter.steps.models.Verification;
import io.starter.steps.telegram.LoginPageSteps;

public class LoginPageVerification extends Verification<LoginPageSteps, LoginPage> {

  public LoginPageVerification(LoginPageSteps steps, LoginPage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public LoginPageVerification verifyThatPageIsOpened() {
    page.getLoginBtn().shouldBe(Condition.visible);
    return this;
  }
}
