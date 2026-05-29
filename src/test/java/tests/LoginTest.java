package tests;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;
import utils.JsonReader;

public class LoginTest extends BaseTest {

    // 1. تعريف الـ DataProvider وربطه بملف القراءة
    @DataProvider(name = "LoginData")
    public Object[][] passData() {
        return JsonReader.getLoginData();
    }

    // 2. ربط الـ Test بالـ DataProvider، وتمرير المتغيرات كـ Parameters
    @Test(dataProvider = "LoginData")
    public void testLoginWithMultipleUsers(String email, String password,String scenarioType) {

        // التوجه إلى صفحة تسجيل الدخول
        String baseUrl = ConfigReader.getProperty("baseUrl");
        page.navigate(baseUrl + "login");
        // استخدام الـ Page Object
        LoginPage loginPage = new LoginPage(page);

        // تمرير البيانات المتغيرة بدلاً من البيانات الثابتة (Hardcoded)
        loginPage.loginUser(email, password);

        if (scenarioType.equalsIgnoreCase("valid")) {
            // التحقق في حالة النجاح: نتأكد إن زرار Logout ظهر
            PlaywrightAssertions.assertThat(loginPage.getLogoutLink()).isVisible();

            // وممكن كمان نتأكد إن الرابط اتغير للصفحة الرئيسية
            PlaywrightAssertions.assertThat(page).hasURL("https://automationexercise.com/");

        } else if (scenarioType.equalsIgnoreCase("invalid")) {
            // التحقق في حالة الفشل: نتأكد إن رسالة الخطأ ظهرت
            PlaywrightAssertions.assertThat(loginPage.getErrorMessage()).isVisible();

            // ونتأكد إن النص اللي جواها مظبوط
            PlaywrightAssertions.assertThat(loginPage.getErrorMessage())
                    .containsText("Your email or password is incorrect!");
        }


            }
}