package io.starter;

import io.starter.config.AppConfig;
import io.starter.config.BrowserConfig;
import io.starter.ui.poe_ninja.PoeNinjaNavigationSteps;
import io.starter.ui.telegram.TelegramNavigationSteps;
import io.starter.utils.Browser;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

public abstract class BaseUiTest extends BaseTest {

  protected TelegramNavigationSteps telegramNavigationSteps = new TelegramNavigationSteps(AppConfig.TELEGRAM_BASE_URL);
  protected PoeNinjaNavigationSteps poeNinjaNavigationSteps = new PoeNinjaNavigationSteps(AppConfig.POE_NINJA_BASE_URL);

  @BeforeSuite(alwaysRun = true)
  protected void baseSetup() {
    Configuration.headless = BrowserConfig.IS_REMOTE;
    Browser.setDefaultConfigurations();
  }
}
