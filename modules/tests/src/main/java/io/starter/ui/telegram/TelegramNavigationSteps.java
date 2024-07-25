package io.starter.ui.telegram;

import io.starter.steps.models.Navigation;

import io.qameta.allure.Step;

public class TelegramNavigationSteps extends Navigation {

  public TelegramNavigationSteps(String baseUrl) {
    super(baseUrl);
  }

  @Step("Open 'Telegram' login page")
  public LoginPageSteps openLoginPage() {
    return new LoginPageSteps(openPage(LoginPage.class));
  }

  @Step("Open 'Telegram' home page")
  public HomePageSteps openHomePage() {
    return new HomePageSteps(openPage(HomePage.class));
  }
}
