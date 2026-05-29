package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {

    // 1. تعريف الـ Page
    private final Page page;

    // 2. تعريف الـ Locators كـ private (تطبيق مبدأ الـ Encapsulation)
    private final Locator loginEmailInput;
    private final Locator loginPasswordInput;
    private final Locator loginButton;
    private final Locator logoutLink;
    private final Locator errorMessage;
    // 3. الـ Constructor لتهيئة الـ Page والـ Locators
    public LoginPage(Page page) {
        this.page = page;

        // Locators الخاصة بجزء تسجيل الدخول (Login)
        this.loginEmailInput = page.locator("form").filter(new Locator.FilterOptions().setHasText("Login")).getByPlaceholder("Email Address");
        this.loginPasswordInput = page.getByPlaceholder("Password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));

        // زرار الـ Logout بيظهر بعد النجاح
        this.logoutLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Logout"));

        // رسالة الخطأ اللي بتظهر لو البيانات غلط
        this.errorMessage = page.getByText("Your email or password is");
    }

    // 4. الـ Actions / Methods (العمليات اللي يقدر اليوزر يعملها في الصفحة)

    public void enterLoginEmail(String email) {
        loginEmailInput.fill(email);
    }

    public void enterLoginPassword(String password) {
        loginPasswordInput.fill(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    // 3. نعمل Methods ترجع الـ Locator نفسه عشان نستخدمه في الـ Assertions
    public Locator getLogoutLink() {
        return logoutLink;
    }

    public Locator getErrorMessage() {
        return errorMessage;
    }


    // دالة مجمعة (Convenience Method) لتسريع كتابة الـ Tests
    public void loginUser(String email, String password) {
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
    }
}


