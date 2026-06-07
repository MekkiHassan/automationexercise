package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;

public class CartPage {
    private final Page page;

    public CartPage(Page page) {
        this.page = page;
    }

    public void openCart() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart")).first().click();
    }

    // ميثود لجلب صف المنتج بناءً على اسمه (الأساس لأي عملية في السلة)
    public Locator getProductRow(String productName) {
        return page.locator("#cart_info_table tbody tr").filter(new Locator.FilterOptions().setHasText(productName));
    }

    // ميثود لجلب سعر المنتج من صفه
    public String getProductPrice(String productName) {
        return getProductRow(productName).locator(".cart_price").textContent().trim();
    }

    // ميثود لجلب إجمالي السعر وتحويله لرقم (Integer)
    public int getTotalPrice() {
        // بنجيب النص \"Rs. 1000\"
        String totalText = page.locator(".cart_total_price").last().textContent().trim();
        // بنشيل أي حاجة مش رقم (زي الـ Rs والمسافات) ونحول النص لرقم
        return Integer.parseInt(totalText.replaceAll("[^0-9]", ""));
    }

    // ميثود حذف منتج
    public void removeProduct(String productName) {
        getProductRow(productName).locator(".cart_quantity_delete").click();
    }
    public void clearCart() {
        // اضغط على زرار الحذف لكل منتج موجود في الجدول
        Locator deleteButtons = page.locator(".cart_quantity_delete");
        while (deleteButtons.count() > 0) {
            deleteButtons.first().click();
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        }
    }

    // ميثود التحقق
    public void verifyProductIsAdded(String productName) {
        PlaywrightAssertions.assertThat(getProductRow(productName)).isVisible();
    }

    // ميثود للتحقق من أن السلة فارغة
    public void verifyCartIsEmpty() {
        PlaywrightAssertions.assertThat(page.locator("#empty_cart")).isVisible();
    }
}