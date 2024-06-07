package io.starter.pages.telegram;

import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.starter.annotations.PageUrl;
import io.starter.steps.models.Page;

@PageUrl("/k")
public class HomePage implements Page {

  private SelenideElement searchInput = $(byCssSelector(".input-search"));

  public SelenideElement getSearchInput() {
    return searchInput;
  }
}
