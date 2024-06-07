package io.starter;

import com.codeborne.selenide.Configuration;
import io.starter.config.BrowserConfig;
//import io.starter.listeners.BrowserListener;
import io.starter.utils.Browser;
import org.testng.annotations.BeforeSuite;
//import org.testng.annotations.Listeners;

//@Listeners({BrowserListener.class})
public abstract class BaseUITest extends BaseTest {

  @BeforeSuite(alwaysRun = true)
  protected void baseSetup() {
    Configuration.headless = BrowserConfig.IS_REMOTE;
    Browser.setDefaultConfigurations();
  }
}
