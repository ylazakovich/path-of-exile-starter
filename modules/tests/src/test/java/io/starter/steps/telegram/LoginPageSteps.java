package io.starter.steps.telegram;

import io.qameta.allure.Step;
import io.starter.pages.telegram.LoginPage;
import io.starter.steps.models.PageSteps;
import io.starter.verifications.telegram.LoginPageVerification;

public class LoginPageSteps extends PageSteps<LoginPageSteps, LoginPageVerification, LoginPage> {

  public LoginPageSteps(LoginPage page) {
    super.verification = new LoginPageVerification(this, page);
    super.page = page;
  }

  @Step("Click login by Phone")
  public LoginPageSteps clickLoginWithPhone() {
    return this;
  }
}
