package io.starter.tests.telegram;

import io.starter.BaseUiTest;
import io.starter.utils.Browser;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseUiTest {

  @Test(description = "Web telegram should be available for login")
  void testGetWebApp() {
    telegramNavigationSteps.openLoginPage()
        .verify()
        .verifyLoginDisplayed();
  }

  @AfterClass
  void tearDown() {
    Browser.closeWebDriver();
  }
}
