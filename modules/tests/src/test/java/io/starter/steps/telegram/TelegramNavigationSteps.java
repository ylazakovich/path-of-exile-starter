package io.starter.steps.telegram;

import io.qameta.allure.Step;
import io.starter.pages.telegram.HomePage;
import io.starter.pages.telegram.LoginPage;
import io.starter.steps.models.Navigation;

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
