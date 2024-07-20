package io.starter.pages.telegram;

import static com.codeborne.selenide.Selenide.$$x;

import com.codeborne.selenide.ElementsCollection;
import io.starter.annotations.PageUrl;

import com.codeborne.selenide.Container;
import org.openqa.selenium.support.FindBy;

@PageUrl("/k")
public class SearchPage extends HomePage {

  @FindBy(xpath = "//div[@class='search-super']")
  private SearchBlock searchBlock;

  public void selectChat(int index) {
    searchBlock.chats.get(index).click();
  }

  private static class SearchBlock implements Container {

    private ElementsCollection chats = $$x("//div[@class='search-group__name']/span[text()='Chats']/../following-sibling::ul[@class='chatlist']");
  }
}
