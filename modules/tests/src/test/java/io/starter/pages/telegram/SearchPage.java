package io.starter.pages.telegram;

import io.starter.annotations.PageUrl;

import com.codeborne.selenide.Container;
import com.codeborne.selenide.ElementsCollection;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

@PageUrl("/k")
public class SearchPage extends HomePage {

  @FindBy(xpath = "//div[@class='search-super']")
  private SearchBlock searchBlock;

  public void selectChat(String user) {
    // TODO: here shoyld implementation of func with selecting chat
  }

  private static class SearchBlock implements Container {

    @FindBy(how = How.XPATH, using = "//div[@class='search-group__name']/span[text()='Chats']/../following-sibling::ul[@class='chatlist']")
    private ElementsCollection chats;
  }
}
