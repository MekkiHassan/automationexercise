package tests;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;
import utils.JsonReader;
import utils.ConfigReader;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class HomePageTest extends BaseTest {

    @DataProvider(name = "categoryData")
    public Object[][] categoryData() {
        Map<String, Map<String, String>> data = JsonReader.readAsNestedMap("categories.json");
        Object[][] dataArray = new Object[data.size()][2];
        int i = 0;
        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            dataArray[i][0] = entry.getKey();
            dataArray[i][1] = entry.getValue().get("expectedText");
            i++;
        }
        return dataArray;
    }

    @Test(dataProvider = "categoryData")
    public void checkFormFilterAreWorkingCorrectly(String category, String expectedText) {
        // نستخدم page.get() للحصول على الصفحة الخاصة بالـ Thread الحالي
        page.get().navigate(ConfigReader.getProperty("baseUrl") + "products");

        HomePage homePage = new HomePage(page.get());

        homePage.selectCategory(category);

        PlaywrightAssertions.assertThat(homePage.getSubCategory(expectedText)).isVisible();
    }

    @DataProvider(name = "productData")
    public Object[][] productData() {
        List<Map<String, String>> products = JsonReader.readProducts("products.json");
        Object[][] dataArray = new Object[products.size()][1];
        for (int i = 0; i < products.size(); i++) {
            dataArray[i][0] = products.get(i).get("name");
        }
        return dataArray;
    }

    @Test(dataProvider = "productData")
    public void testAddMultipleProductsToCart(String productName) {
        // نستخدم page.get() في كل الأماكن
        page.get().navigate(ConfigReader.getProperty("baseUrl") + "products");

        HomePage homePage = new HomePage(page.get());
        CartPage cartPage = new CartPage(page.get());

        // إضافة المنتج
        homePage.addToCart(productName);

        // الذهاب للسلة (السيناريو ب: الضغط على الرابط في الـ Navbar)
        cartPage.openCart();

        // التحقق
        cartPage.verifyProductIsAdded(productName);
    }
}