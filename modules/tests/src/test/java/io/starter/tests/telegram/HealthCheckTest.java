package io.starter.tests.telegram;


import io.starter.BaseUITest;
import io.starter.utils.Browser;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseUITest {

  @Test()
  void testGetWebApp() {
    telegramNavigationSteps.openLoginPage()
        .verify()
        .verifyLoginDisplayed();
  }

  @Test()
  void testGetWebApp2() {
    telegramNavigationSteps.openLoginPage()
        .verify()
        .verifyLoginDisplayed();
  }

  @AfterClass
  void tearDown() {
    Browser.closeWebDriver();
  }
}
