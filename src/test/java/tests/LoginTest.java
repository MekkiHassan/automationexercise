package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.JsonReader;

public class LoginTest extends BaseTest {

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        // سياق جديد تماماً بدون كوكيز اللوجن
        context.set(browser.get().newContext());
        blockAds(context.get()); // حجب الإعلانات هنا أيضاً
        page.set(context.get().newPage());
    }

    @Test(dataProvider = "LoginData", dataProviderClass = JsonReader.class)
    public void testLoginWithMultipleUsers(String email, String password, String scenarioType) {
        page.get().navigate(config.getProperty("baseUrl") + "login");
        LoginPage loginPage = new LoginPage(page.get());

        if (scenarioType.equalsIgnoreCase("valid")) {
            HomePage homePage = loginPage.loginUser(email, password);
            PlaywrightAssertions.assertThat(homePage.getLogoutLink()).isVisible();
        } else {
            loginPage.loginUser(email, password);
            PlaywrightAssertions.assertThat(loginPage.getErrorMessage()).isVisible();
        }
    }
}