package io.starter.ui.poe_ninja;

import io.starter.steps.models.Navigation;
import io.starter.ui.poe_ninja.pages.HomePage;
import io.starter.ui.poe_ninja.steps.HomePageSteps;

import io.qameta.allure.Step;

public class PoeNinjaNavigationSteps extends Navigation {

  public PoeNinjaNavigationSteps(String baseUrl) {
    super(baseUrl);
  }

  @Step("Open 'poe.ninja' home page")
  public HomePageSteps openHomePage() {
    return new HomePageSteps(openPage(HomePage.class));
  }
}
