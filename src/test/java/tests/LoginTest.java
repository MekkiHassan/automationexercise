package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.JsonReader;

public class LoginTest extends BaseTest {

    @DataProvider(name = "LoginData")
    public Object[][] passData() {
        return JsonReader.getLoginData();
    }

    @Test(dataProvider = "LoginData")
    public void testLoginWithMultipleUsers(String email, String password, String scenarioType) {
        // تعديل: استخدام page.get() للوصول للصفحة الخاصة بالـ Thread الحالي
        page.get().navigate(ConfigReader.getProperty("baseUrl") + "login");

        // تعديل: تمرير page.get() للـ Constructor الخاص بـ LoginPage
        LoginPage loginPage = new LoginPage(page.get());

        if (scenarioType.equalsIgnoreCase("valid")) {
            // نستخدم الـ Chain هنا
            HomePage homePage = loginPage.loginUser(email, password);

            // التحقق (Assertion)
            PlaywrightAssertions.assertThat(homePage.getLogoutLink()).isVisible();
        } else {
            // في حالة البيانات الخاطئة، الـ loginUser هيرجع نفس الـ loginPage أو null
            loginPage.loginUser(email, password);

            // ونعمل Assertion على نفس صفحة الـ Login
            PlaywrightAssertions.assertThat(loginPage.getErrorMessage()).isVisible();
        }
    }
}