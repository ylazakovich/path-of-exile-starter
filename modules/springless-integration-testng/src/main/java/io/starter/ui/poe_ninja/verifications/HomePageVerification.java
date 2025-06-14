package io.starter.ui.poe_ninja.verifications;

import io.starter.steps.models.Verification;
import io.starter.ui.poe_ninja.pages.HomePage;
import io.starter.ui.poe_ninja.steps.HomePageSteps;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

public class HomePageVerification extends Verification<HomePageSteps, HomePage> {

  public HomePageVerification(HomePageSteps steps, HomePage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public HomePageVerification verifySearchDisplayed() {
    page.getSearch().shouldBe(Condition.visible);
    return this;
  }
}
