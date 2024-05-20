package io.starter.utils;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.starter.config.BrowserConfig;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.MutableCapabilities;

@UtilityClass
public class Browser {

  /**
   * Set default configurations to browser.
   */
  public static void setDefaultConfigurations() {
    Configuration.browser = BrowserConfig.BROWSER;
    Configuration.fileDownload = FileDownloadMode.FOLDER;
    Configuration.screenshots = false;
  }

  /**
   * Set remote configurations to browser.
   */
  public static void setRemoteDefaultConfiguration() {
    Configuration.remote = BrowserConfig.REMOTE_URL;
    setDefaultCapabilities();
  }

  /**
   * Merge two {@link Capabilities} together and return the union of the two as a new {@link Capabilities} instance.
   * Capabilities from {@code other} will override those in {@code this}.
   *
   * @param capabilities {@link Capabilities} from selenium lib
   * @return {@link MutableCapabilities} mutable obj
   */
  public static MutableCapabilities mergeCapabilities(Capabilities capabilities) {
    return Configuration.browserCapabilities.merge(capabilities);
  }

  /**
   * Set default capabilities.
   */
  public static void setDefaultCapabilities() {
    Configuration.browserCapabilities.setCapability("selenoid:options", getDefaultSelenoidOptions());
  }

  /**
   * Get default Selenoid options.
   *
   * @return {@link Map} of options
   */
  public static Map<String, Object> getDefaultSelenoidOptions() {
    return Map.of(
        "sessionTimeout", "5m",
        "enableLog", true,
        "enableVNC", true);
  }

  /**
   * Close web driver.
   */
  public static void closeWebDriver() {
    Selenide.closeWebDriver();
  }

  /**
   * Get url from browser.
   */
  public static String getUrl() {
    return WebDriverRunner.url();
  }

  /**
   * Refresh browser active page.
   */
  public static void refresh() {
    Selenide.refresh();
  }

  /**
   * Click on browser back button.
   */
  public static void back() {
    Selenide.back();
  }

  /**
   * Get cookie from browser by name.
   *
   * @param cookieName cookie name as {@link String}
   * @return {@link Cookie}
   */
  public static Cookie getCookie(String cookieName) {
    return WebDriverRunner.getWebDriver().manage().getCookieNamed(cookieName);
  }

  /**
   * Add cookie to browser.
   *
   * @param cookie {@link Cookie}
   */
  public static void addCookie(Cookie cookie) {
    WebDriverRunner.getWebDriver().manage().addCookie(cookie);
  }

  /**
   * Clear all browser cookies.
   */
  public static void clearCookies() {
    Selenide.clearBrowserCookies();
  }

  /**
   * Confirm browser alert and add comment.
   *
   * @param comment comment as {@link String}
   */
  public static void confirmAlertWithComment(String comment) {
    confirmAlert();
    addCommentToAlert(comment);
  }

  /**
   * Confirm browser alert.
   */
  public static void confirmAlert() {
    Selenide.confirm();
  }

  /**
   * Confirm browser alert with dialog text.
   *
   * @param expectedDialogText dialog text as {@link String}
   */
  public static void confirmAlert(String expectedDialogText) {
    Selenide.confirm(expectedDialogText);
  }

  /**
   * Dismiss browser alert.
   */
  public static void dismissAlert() {
    Selenide.dismiss();
  }

  /**
   * Dismiss browser alert with dialog text.
   *
   * @param expectedDialogText dialog text as {@link String}
   */
  public static void dismissAlert(String expectedDialogText) {
    Selenide.dismiss(expectedDialogText);
  }

  /**
   * Get browser alert dialog text.
   *
   * @return dialog text as {@link String}
   */
  public static String getAlertText() {
    return Selenide.switchTo().alert().getText();
  }

  /**
   * Add comment to browser alert.
   *
   * @param comment comment as {@link String}
   */
  public static void addCommentToAlert(String comment) {
    Selenide.prompt(comment);
  }

  /**
   * Switch to first frame with SLOW duration.
   */
  public static void switchToFirstFrame() {
    switchToFrame(0, Duration.ofSeconds(120));
  }

  /**
   * Switch to frame.
   *
   * @param frameIndex switched frame index. Note: Started from 0 index.
   * @param duration   timeout duration.
   */
  public static void switchToFrame(int frameIndex, Duration duration) {
    Selenide.switchTo().frame(frameIndex, duration);
  }

  /**
   * Switch to frame by SelenideElement.
   *
   * @param selenideElement switched frame element.
   */
  public static void switchToFrame(SelenideElement selenideElement) {
    Selenide.switchTo().frame(selenideElement.should(Condition.appear, Duration.ofSeconds(120)));
  }

  /**
   * Check that web driver started and browser openned.
   *
   * @return true or false
   */
  public static boolean isOpened() {
    return WebDriverRunner.hasWebDriverStarted();
  }

  /**
   * Check that it is remote connection.
   *
   * @return true or false
   */
  public static boolean isRemote() {
    return Objects.nonNull(Configuration.remote);
  }
}
