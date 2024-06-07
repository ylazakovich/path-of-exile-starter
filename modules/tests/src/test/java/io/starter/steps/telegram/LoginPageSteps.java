package io.starter.steps.telegram;

import io.qameta.allure.Step;
import io.starter.pages.telegram.HomePage;
import io.starter.pages.telegram.LoginPage;
import io.starter.steps.models.PageSteps;
import io.starter.verifications.telegram.LoginPageVerification;

public class LoginPageSteps extends PageSteps<LoginPageSteps, LoginPageVerification, LoginPage> {

  public LoginPageSteps(LoginPage page) {
    super.verification = new LoginPageVerification(this, page);
    super.page = page;
  }

  @Step("Login by Phone")
  public LoginPageSteps loginViaPhone(String phone) {
    page.clickPrimaryBtn();
    page.enterPhone(phone);
    page.clickNextBtn();
    return this;
  }

  @Step("Input verification code")
  public HomePageSteps inputVerificationCode(String code) {
    page.inputCode(code);
    return new HomePageSteps(new HomePage());
  }
}
