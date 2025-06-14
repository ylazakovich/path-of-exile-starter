package io.starter.ui.telegram.verifications;

import io.starter.steps.models.Verification;
import io.starter.ui.telegram.pages.SearchPage;
import io.starter.ui.telegram.steps.SearchPageSteps;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;

public class SearchPageVerification extends Verification<SearchPageSteps, SearchPage> {

  public SearchPageVerification(SearchPageSteps steps, SearchPage page) {
    super(steps, page);
  }

  @Step("Verify that page is successfully opened")
  public SearchPageVerification verifySearchDisplayed() {
    page.getSearch().shouldBe(Condition.visible);
    return this;
  }
}
