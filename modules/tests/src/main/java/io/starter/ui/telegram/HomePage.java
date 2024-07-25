package io.starter.ui.telegram;

import io.starter.annotations.PageUrl;
import io.starter.steps.models.Page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

@PageUrl("/k")
public class HomePage implements Page {

  private SelenideElement search = $(".input-search");
  private SelenideElement openedChat = $(".chat-background");

  public SelenideElement getSearch() {
    return search;
  }

  public SelenideElement getOpenedChat() {
    return openedChat;
  }

  public SearchPage search(String text) {
    Selenide.actions().sendKeys(search, text).perform();
    return Selenide.page(SearchPage.class);
  }
}
