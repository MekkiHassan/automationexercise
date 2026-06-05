package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.JsonReader; // تأكد من الـ Import الصحيح
import java.util.Map;
import java.util.regex.Pattern;

public class HomePage {

    private final Page page;
    private final Locator logoutLink;
    // هنا الربط بالـ JSON: نقرأ البيانات كـ Map
    private final Map<String, Map<String, String>> categoryLinks
            = JsonReader.readAsNestedMap("categories.json");

    public HomePage(Page page) {
        this.page = page;
        this.logoutLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Logout"));
    }

    public Locator getLogoutLink() {
        return logoutLink;
    }

    public void selectCategory(String categoryName) {
        // الآن نحن نقرأ من الـ Nested Map
        Map<String, String> categoryData = categoryLinks.get(categoryName.toUpperCase());
        String href = categoryData.get("href"); // استخراج الـ href من الـ Object

        page.locator(".left-sidebar")
                .locator("a[href='" + href + "']")
                .click();
    }
    public Locator getSubCategory(String subCategoryName) {
        return page.locator(".left-sidebar")
                .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions()
                        .setName(Pattern.compile(".*" + subCategoryName + ".*", Pattern.CASE_INSENSITIVE)))
                .first();
    }

    public void addToCart(String productName) {
        // 1. الضغط على زر الإضافة
        String xpath = "//div[contains(@class, 'single-products')]//p[normalize-space()='" + productName + "']/following-sibling::a[contains(@class, 'add-to-cart')]";
        page.locator(xpath).first().click(new Locator.ClickOptions().setForce(true));

        // 2. إغلاق المودال بالضغط على "Continue Shopping"
        // ننتظر المودال يظهر لثانية، ثم نضغط إغلاق
        page.locator(".modal-footer .btn-success").waitFor();
        page.locator(".modal-footer .btn-success").click();

        // 3. ننتظر المودال يختفي تماماً (عشان نتأكد إننا جاهزين للخطوة اللي بعدها)
        page.locator("#cartModal").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

}