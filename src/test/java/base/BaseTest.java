package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class BaseTest {
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();
    protected static Properties config = new Properties();

    static {
        try (InputStream input = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) config.load(input);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void blockAds(BrowserContext ctx) {
        ctx.route("**/*", route -> {
            String url = route.request().url();
            if (url.contains("googleads") || url.contains("googlesyndication") || url.contains("doubleclick")) {
                route.abort();
            } else {
                route.resume();
            }
        });
    }

    public static void generateAuthFile() {
        Playwright pw = Playwright.create();
        Browser br = pw.chromium().launch();
        BrowserContext ctx = br.newContext();
        blockAds(ctx);
        Page pg = ctx.newPage();
        pg.navigate(config.getProperty("baseUrl") + "login");
        pg.locator("form").filter(new com.microsoft.playwright.Locator.FilterOptions().setHasText("Login")).getByPlaceholder("Email Address").fill("Mekki@gmail.com");
        pg.getByPlaceholder("Password").fill("123456");
        pg.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        pg.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        ctx.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("auth.json")));
        ctx.close(); br.close(); pw.close();
    }

    @BeforeMethod
    public void setUp() {
        Playwright pw = Playwright.create();

        // الحل السينيور:
        // لو إحنا في GitHub Actions (عن طريق التأكد من وجود متغير بيئة CI)، اجعل الـ Headless = true
        boolean isCI = System.getenv("CI") != null;
        boolean headlessConfig = Boolean.parseBoolean(config.getProperty("headless", "true"));

        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(isCI || headlessConfig); // لو CI، هيرن Headless غصباً عنه

        Browser br = pw.chromium().launch(options);
        playwright.set(pw);
        browser.set(br);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (page.get() != null) {
            if (result.getStatus() == ITestResult.FAILURE) saveScreenshot(page.get().screenshot());
            page.get().close();
        }
        if (context.get() != null) context.get().close();
        if (browser.get() != null) browser.get().close();
        if (playwright.get() != null) playwright.get().close();
        page.remove(); context.remove(); browser.remove(); playwright.remove();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] saveScreenshot(byte[] screenShot) { return screenShot; }
}