package io.starter.listeners;

import javax.annotation.ParametersAreNonnullByDefault;

import io.starter.utils.Browser;

import org.testng.ITestResult;
import org.testng.internal.ExitCodeListener;

/**
 * Annotate your test class with {@code @Listeners({ BrowserPerTest.class})}
 */
@ParametersAreNonnullByDefault
public class BrowserListener extends ExitCodeListener {

  @Override
  public void onTestFailure(ITestResult result) {
    super.onTestFailure(result);
    Browser.closeWebDriver();
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    super.onTestFailedButWithinSuccessPercentage(result);
    Browser.closeWebDriver();
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    super.onTestSuccess(result);
    Browser.closeWebDriver();
  }
}
