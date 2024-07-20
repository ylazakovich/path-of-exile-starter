package io.starter.pages.telegram;

import io.starter.annotations.PageUrl;

import com.codeborne.selenide.Container;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$$x;

@PageUrl("/k")
public class SearchPage extends HomePage {

  @FindBy(xpath = "//div[@class='search-super']")
  private SearchBlock searchBlock;

  public void selectChat(int index) {
    searchBlock.chats.get(index).click();
  }

  private static class SearchBlock implements Container {

    private final String chatXpath = "//div[@class='search-group__name']/span[text()='Chats']";
    private ElementsCollection chats = $$x(chatXpath);
  }
}
