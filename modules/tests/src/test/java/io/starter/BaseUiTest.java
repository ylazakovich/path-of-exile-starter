package io.starter;

import io.starter.config.BrowserConfig;
import io.starter.utils.Browser;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

public abstract class BaseUiTest extends BaseTest {

  @BeforeSuite(alwaysRun = true)
  protected void baseSetup() {
    Configuration.headless = BrowserConfig.IS_REMOTE;
    Browser.setDefaultConfigurations();
  }
}
