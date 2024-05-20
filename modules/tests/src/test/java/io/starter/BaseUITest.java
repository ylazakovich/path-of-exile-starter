package io.starter;

import io.starter.listeners.BrowserListener;
import io.starter.utils.Browser;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({BrowserListener.class})
public abstract class BaseUITest extends BaseTest {

  @BeforeSuite(alwaysRun = true)
  // TODO: Later can be implemented Listeners over injections
  protected void baseSetup() {
//    Configuration.headless = true;
    Browser.setDefaultConfigurations();
  }
}
