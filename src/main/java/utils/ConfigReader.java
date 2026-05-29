package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    // استخدام static block عشان الملف يتقرأ مرة واحدة بس أول ما الكلاس يشتغل
    static {
        try {
            String filePath = "src/test/resources/config.properties";
            FileInputStream inputStream = new FileInputStream(filePath);
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("حدث خطأ أثناء قراءة ملف الإعدادات!");
            e.printStackTrace();
        }
    }

    // دالة بترجع قيمة الـ Key اللي بنطلبه
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}