package io.starter.ui.poe_ninja.steps;

import io.starter.steps.models.PageSteps;
import io.starter.ui.poe_ninja.pages.HomePage;
import io.starter.ui.poe_ninja.verifications.HomePageVerification;

public class HomePageSteps extends PageSteps<HomePageSteps, HomePageVerification, HomePage> {

  public HomePageSteps(HomePage page) {
    super.verification = new HomePageVerification(this, page);
    super.page = page;
  }
}
