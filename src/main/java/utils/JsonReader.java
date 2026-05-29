package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;

public class JsonReader {

    // دالة لقراءة الداتا من ملف JSON وتحويلها لـ 2D Array
    public static Object[][] getLoginData() {
        try {
            // 1. تحديد مسار ملف الـ JSON
            FileReader reader = new FileReader("src/test/resources/loginData.json");

            // 2. تحويل الملف إلى JsonArray
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // 3. إنشاء مصفوفة ثنائية الأبعاد (صفوف وأعمدة) لتخزين البيانات
            // عدد الصفوف = عدد عناصر الـ JSON (عدد السيناريوهات)
            // عدد الأعمدة = 2 (الإيميل، الباسورد)
            Object[][] data = new Object[jsonArray.size()][3];

            // 4. تعبئة المصفوفة بالبيانات من الـ JSON
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObj = jsonArray.get(i).getAsJsonObject();
                data[i][0] = jsonObj.get("email").getAsString();
                data[i][1] = jsonObj.get("password").getAsString();
                data[i][2] = jsonObj.get("scenarioType").getAsString();
            }

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}