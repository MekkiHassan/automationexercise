package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    private final Page page;
    private final Locator loginEmailInput;
    private final Locator loginPasswordInput;
    private final Locator loginButton;
    private final Locator logoutLink;
    private final Locator errorMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.loginEmailInput = page.locator("form").filter(new Locator.FilterOptions().setHasText("Login")).getByPlaceholder("Email Address");
        this.loginPasswordInput = page.getByPlaceholder("Password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.logoutLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Logout"));
        this.errorMessage = page.locator("form[action='/login'] p[style*='color: red']"); // عدلت اللوكيتور عشان يكون أكثر دقة
    }

    public HomePage loginUser(String email, String password) {
        loginEmailInput.fill(email);
        loginPasswordInput.fill(password);
        loginButton.click();

        // التحقق من النجاح قبل إرجاع الـ HomePage
        if (page.url().contains("login")) {
            return null; // فشل اللوجن
        }
        return new HomePage(page);
    }

    public Locator getLogoutLink() { return logoutLink; }
    public Locator getErrorMessage() { return errorMessage; }
}