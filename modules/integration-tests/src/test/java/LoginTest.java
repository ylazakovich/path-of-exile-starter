import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LoginTest {

  @AfterMethod
  public void tearDown() {
    Selenide.closeWebDriver();
  }

  @Test(enabled = false)
  public void testLogin() {
    Selenide.open("https://web.telegram.org/k");
    SelenideElement byPhoneBtn = $(Selectors.byCssSelector("button[class^='btn-primary']"));
    byPhoneBtn.click();
    // TODO: Need to make a controller with assigning code for auth then continue test
  }
}
