package io.starter.steps.telegram;

import io.starter.pages.telegram.HomePage;
import io.starter.steps.models.PageSteps;
import io.starter.verifications.telegram.HomePageVerification;

import com.codeborne.selenide.Selenide;

public class HomePageSteps extends PageSteps<HomePageSteps, HomePageVerification, HomePage> {

  public HomePageSteps(HomePage page) {
    super.verification = new HomePageVerification(this, page);
    super.page = page;
  }

  public SearchPageSteps search(String query) {
    page.search(query);
    return new SearchPageSteps(Selenide.page());
  }
}
