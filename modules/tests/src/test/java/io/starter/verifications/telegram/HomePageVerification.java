package io.starter.verifications.telegram;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import io.starter.pages.telegram.HomePage;
import io.starter.steps.models.Verification;
import io.starter.steps.telegram.HomePageSteps;

public class HomePageVerification extends Verification<HomePageSteps, HomePage> {

  public HomePageVerification(HomePageSteps steps, HomePage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public HomePageVerification verifyThatPageIsOpened() {
    page.getLoginBtnWithPhone().shouldBe(Condition.visible);
    return this;
  }
}
