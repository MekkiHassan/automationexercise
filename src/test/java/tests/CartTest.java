package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;

import java.nio.file.Paths;

public class CartTest extends BaseTest {
    private CartPage cartPage;
    private HomePage homePage;

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        // 1. التجهيز
        context.set(browser.get().newContext(new com.microsoft.playwright.Browser.NewContextOptions().setStorageStatePath(Paths.get("auth.json"))));
        blockAds(context.get());
        page.set(context.get().newPage());

        // 2. التهيئة
        this.cartPage = new CartPage(page.get());
        this.homePage = new HomePage(page.get());

        // 3. الخطوة "السينيور": تنظيف السلة قبل بداية أي تيست
        page.get().navigate(config.getProperty("baseUrl") + "view_cart");
        this.cartPage.clearCart();
    }

    @Test
    public void testCartScenario() {
        // 1. التنقل لصفحة المنتجات
        page.get().navigate(config.getProperty("baseUrl") );

        // 2. إضافة منتج (اختياري: لو السلة فاضية)
        homePage.addToCart("Blue Top");

        // 3. فتح السلة
        cartPage.openCart();

        // 4. التحقق
        cartPage.verifyProductIsAdded("Blue Top");
        int actualPrice = cartPage.getTotalPrice();

        // هنا بتختبر لو السعر اللي ظهر في السلة بيساوي القيمة اللي إنت متوقعها
        org.testng.Assert.assertEquals(actualPrice, 500, "السعر مش مظبوط يابرووو...!");
    }
}