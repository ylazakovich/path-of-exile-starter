package io.starter.ui.telegram;

import io.starter.steps.models.Navigation;

import io.qameta.allure.Step;
import io.starter.ui.telegram.pages.HomePage;
import io.starter.ui.telegram.pages.LoginPage;
import io.starter.ui.telegram.steps.HomePageSteps;
import io.starter.ui.telegram.steps.LoginPageSteps;

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
