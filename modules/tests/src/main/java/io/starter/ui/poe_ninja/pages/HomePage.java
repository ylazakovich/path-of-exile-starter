package io.starter.ui.poe_ninja.pages;

import io.starter.annotations.PageUrl;
import io.starter.steps.models.Page;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@PageUrl("/")
public class HomePage implements Page {

  @Getter
  private SelenideElement search = $(By.id("downshift-0-input"));
}
