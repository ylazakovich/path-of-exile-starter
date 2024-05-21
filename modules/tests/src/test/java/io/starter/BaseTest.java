package io.starter;

import io.starter.steps.telegram.TelegramNavigationSteps;

abstract class BaseTest {

  // TODO: Here will be initiated STEPS classes
  protected TelegramNavigationSteps telegramNavigationSteps = new TelegramNavigationSteps("https://web.telegram.org");
}
