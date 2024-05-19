import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

  @BeforeMethod
  void setup() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    Configuration.browserCapabilities.merge(options);
    Configuration.headless = true;
  }

  @AfterMethod
  void tearDown() {
    Selenide.closeWebDriver();
  }

  @Test()
  void testLogin() {
    Selenide.open("https://web.telegram.org/k");
    SelenideElement byPhoneBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));
    byPhoneBtn.click();
    // TODO: Need to make a controller with assigning code for auth then continue test
  }
}
