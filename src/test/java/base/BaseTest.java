package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();
    protected static Properties config = new Properties();

    @BeforeClass
    public void loadConfig() throws IOException {
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        config.load(fis);
    }

    @BeforeMethod
    public void setUp() {
        // الأولوية هنا للـ System Property (التي نمررها من الـ GitHub Action)
        String headlessEnv = System.getProperty("headless");
        boolean isHeadless = (headlessEnv != null) ?
                Boolean.parseBoolean(headlessEnv) :
                Boolean.parseBoolean(config.getProperty("headless"));

        Playwright pw = Playwright.create();
        Browser br = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));

        playwright.set(pw);
        browser.set(br);

        BrowserContext ctx = br.newContext();
        context.set(ctx);
        page.set(ctx.newPage());

        // الانتقال للرابط من الـ config
        page.get().navigate(config.getProperty("baseUrl"));

        // إضافة هذا السطر لضمان تحميل الصفحة بالكامل قبل أي خطوة تالية
        page.get().waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (page.get() != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                saveScreenshot(page.get().screenshot());
            }
            page.get().close();
        }
        if (context.get() != null) context.get().close();
        if (browser.get() != null) browser.get().close();
        if (playwright.get() != null) playwright.get().close();

        page.remove();
        context.remove();
        browser.remove();
        playwright.remove();
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] saveScreenshot(byte[] screenShot) {
        return screenShot;
    }
}