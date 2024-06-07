package io.starter.tests.telegram;

import io.starter.BaseUITest;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseUITest {

  @Test()
  void testGetWebApp() {
    telegramNavigationSteps.openLoginPage()
        .verify()
        .verifyLoginDisplayed();
  }
}
