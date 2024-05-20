package io.starter.telegram;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.starter.BaseUITest;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseUITest {

  @Test()
  void testGetWebApp() {
    Selenide.open("https://web.telegram.org/k");
    SelenideElement primaryBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));
    primaryBtn.shouldBe(Condition.interactable);
  }
}
