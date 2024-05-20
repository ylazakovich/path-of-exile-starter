package io.starter;

import com.codeborne.selenide.Configuration;
import io.starter.utils.Browser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseUITest extends BaseTest {

  @BeforeSuite(alwaysRun = true)
  // TODO: Later can be implemented Listeners over injections
  protected void initBrowserConfig() {
    Configuration.headless = true;
    Browser.setDefaultConfigurations();
  }

  @AfterMethod(alwaysRun = true)
  protected void closeWebDriver() {
    // TODO: Need to try close driver over listener
    Browser.closeWebDriver();
  }
}
