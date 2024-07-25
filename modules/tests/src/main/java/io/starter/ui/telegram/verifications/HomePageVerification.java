package io.starter.ui.telegram.verifications;

import io.starter.steps.models.Verification;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import io.starter.ui.telegram.pages.HomePage;
import io.starter.ui.telegram.steps.HomePageSteps;

public class HomePageVerification extends Verification<HomePageSteps, HomePage> {

  public HomePageVerification(HomePageSteps steps, HomePage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public HomePageVerification verifySearchDisplayed() {
    page.getSearch().shouldBe(Condition.visible);
    return this;
  }

  @Step("Verify that chat is exist")
  public HomePageVerification verifyOpenedChat() {
    page.getOpenedChat().shouldBe(Condition.visible);
    return this;
  }
}
