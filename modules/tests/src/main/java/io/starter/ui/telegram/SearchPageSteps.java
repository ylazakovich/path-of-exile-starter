package io.starter.ui.telegram;

import io.starter.steps.models.PageSteps;

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
