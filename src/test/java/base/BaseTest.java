package base;

import com.microsoft.playwright.*;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {

    // الـ Playwright والـ Browser لازم يبقوا ThreadLocal عشان Parallel Execution
    // لو خليتهم Static، الـ Threads هتتخانق عليهم
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        // كل Thread بيفتح الـ Playwright والـ Browser الخاص بيه.. أيوة إنت سمعت صح!
        // ده أبطأ حاجة بسيطة بس مستحيل يقع (Flaky-free)
        Playwright pw = Playwright.create();
        Browser br = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        playwright.set(pw);
        browser.set(br);

        BrowserContext ctx = br.newContext();
        context.set(ctx);
        page.set(ctx.newPage());
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

        // إغلاق الـ Browser والـ Playwright الخاص بالـ Thread ده
        if (browser.get() != null) browser.get().close();
        if (playwright.get() != null) playwright.get().close();

        // تنظيف نهائي
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