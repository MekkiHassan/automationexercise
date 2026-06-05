package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.JsonReader;
import java.util.Map;
import java.util.regex.Pattern;

public class HomePage {

    private final Page page;
    private final Locator logoutLink;
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
        Map<String, String> categoryData = categoryLinks.get(categoryName.toUpperCase());
        String href = categoryData.get("href");

        // حددنا اللينك بالظبط اللي محتاجينه
        Locator categoryLink = page.locator(".left-sidebar").locator("a[href='" + href + "']");

        // التعديل: الانتظار حتى يكون اللينك نفسه مرئي وجاهز للتفاعل لمدة تصل لـ 15 ثانية
        categoryLink.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        categoryLink.click();
    }

    public Locator getSubCategory(String subCategoryName) {
        Locator subCategory = page.locator(".left-sidebar")
                .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions()
                        .setName(Pattern.compile(".*" + subCategoryName + ".*", Pattern.CASE_INSENSITIVE)))
                .first();

        // التعديل: نضمن إن الـ SubCategory ظهرت فعلاً في الـ DOM قبل ما نرجعها للـ Assertion
        subCategory.waitFor(new Locator.WaitForOptions().setTimeout(15000));
        return subCategory;
    }

    public void addToCart(String productName) {
        String xpath = "//div[contains(@class, 'single-products')]//p[normalize-space()='" + productName + "']/following-sibling::a[contains(@class, 'add-to-cart')]";

        // ننتظر المنتج نفسه يظهر الأول قبل ما نضغط عليه
        page.locator(xpath).first().waitFor(new Locator.WaitForOptions().setTimeout(15000));
        page.locator(xpath).first().click(new Locator.ClickOptions().setForce(true));

        page.locator(".modal-footer .btn-success").waitFor();
        page.locator(".modal-footer .btn-success").click();

        page.locator("#cartModal").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }
}