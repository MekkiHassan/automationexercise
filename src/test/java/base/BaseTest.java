package base;

import com.microsoft.playwright.*;
import org.testng.annotations.*;
import utils.ConfigReader;

/*
*  دة عشان اشغل الريكورد من ال cml
* mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen https://automationexercise.com"
*
* */
public class BaseTest {

    // تعريف الكائنات كـ protected لتكون مرئية لكلاسات الاختبار التي ترث من هذا الكلاس
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUpBrowser() {
        playwright = Playwright.create();

        // 1. قراءة الإعدادات من الملف
        String browserName = ConfigReader.getProperty("browser");
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

        String slowMoValue = ConfigReader.getProperty("slowMo");
        double slowMo = (slowMoValue != null) ? Double.parseDouble(slowMoValue) : 0;
        // 2. تطبيق إعدادات التشغيل
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(isHeadless)
                .setSlowMo(slowMo); // ممكن تخلي دي كمان تقرأ من الملف لو حابب

        // 3. اختيار المتصفح المناسب باستخدام Switch Case
        switch (browserName.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            case "chromium":
            default:
                browser = playwright.chromium().launch(launchOptions);
                break;
        }}

    @BeforeMethod
    public void setUpPage() {
        // 2. إنشاء سياق (Context) وصفحة (Page) جديدة تماماً قبل كل طريقة اختبار (@Test)
        // هذا يضمن أن كل اختبار يبدأ ببيئة نظيفة (بدون كوكيز أو كاش من الاختبار السابق)
        context = browser.newContext();
        page = context.newPage();

        // يمكنك هنا إضافة أي إعدادات عامة مثل تكبير الشاشة أو الذهاب للرابط الأساسي
       //  page.navigate("https://automationexercise.com/");
    }

    @AfterMethod
    public void tearDownPage() {
        // 3. إغلاق الصفحة والسياق بعد انتهاء كل اختبار
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
    }

    @AfterClass
    public void tearDownBrowser() {
        // 4. إغلاق المتصفح وإيقاف Playwright بعد الانتهاء من كافة اختبارات الكلاس
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}