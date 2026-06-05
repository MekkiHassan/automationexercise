package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public void openCart() {
        // الضغط على زر السلة الثابت في القائمة العلوية
        // ملاحظة: الرابط في الـ Header غالباً بيكون نصه "Cart" أو "Shopping Cart"
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart")).first().click();
    }

    // ميثود للتحقق من وجود المنتج في السلة
    public void verifyProductIsAdded(String productName) {
        Locator cartItem = page.locator("#cart_info_table")
                .getByText(productName);
        PlaywrightAssertions.assertThat(cartItem).isVisible();
    }

    public boolean isProductInCart(String productName) {
        // نبحث عن اسم المنتج داخل جدول السلة
        return page.locator("#cart_info_table")
                .getByText(productName)
                .isVisible();
    }
}