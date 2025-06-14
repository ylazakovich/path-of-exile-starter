package io.starter.ui.telegram.steps;

import io.starter.steps.models.PageSteps;
import io.starter.ui.telegram.pages.SearchPage;
import io.starter.ui.telegram.verifications.SearchPageVerification;

import com.codeborne.selenide.Selenide;

public class SearchPageSteps extends PageSteps<SearchPageSteps, SearchPageVerification, SearchPage> {

  public SearchPageSteps(SearchPage page) {
    super.verification = new SearchPageVerification(this, page);
    super.page = page;
  }

  public HomePageSteps selectChat(int index) {
    page.selectChat(index);
    return new HomePageSteps(Selenide.page());
  }
}
