package tests;

import base.BaseTest;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import utils.JsonReader;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class HomePageTest extends BaseTest {


    @BeforeSuite(alwaysRun = true)
    public void prepareSession() {
        BaseTest.generateAuthFile();
    }

    @Override
    @BeforeMethod
    public void setUp() {
        super.setUp();
        // استخدام الـ Auth File
        context.set(browser.get().newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("auth.json"))));
        blockAds(context.get());
        page.set(context.get().newPage());
    }

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
        page.get().navigate(config.getProperty("baseUrl"));
        page.get().waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        HomePage homePage = new HomePage(page.get());

        homePage.selectCategory(category);
        PlaywrightAssertions.assertThat(homePage.getSubCategory(expectedText)).isVisible();
    }

    //////////////// ////////

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
        page.get().navigate(config.getProperty("baseUrl") + "products");
        page.get().waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);

        HomePage homePage = new HomePage(page.get());
        CartPage cartPage = new CartPage(page.get());

        homePage.addToCart(productName);
        cartPage.openCart();
        cartPage.verifyProductIsAdded(productName);
    }

}