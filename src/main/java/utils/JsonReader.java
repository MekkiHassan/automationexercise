package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Object[][] getLoginData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // قراءة الملف كقائمة من الـ Maps
            List<Map<String, String>> dataList = mapper.readValue(
                    new File("src/test/resources/loginData.json"),
                    new TypeReference<List<Map<String, String>>>(){}
            );

            // تحويلها إلى Object[][] كما يتطلب الـ DataProvider
            Object[][] data = new Object[dataList.size()][3];
            for (int i = 0; i < dataList.size(); i++) {
                data[i][0] = dataList.get(i).get("email");
                data[i][1] = dataList.get(i).get("password");
                data[i][2] = dataList.get(i).get("scenarioType");
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read login data", e);
        }
    }

    // 1. ميثود لقراءة أي ملف JSON كقائمة من الـ Maps (مناسبة للـ DataProvider)
    public static List<Map<String, String>> readAsList(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<List<Map<String, String>>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON list from: " + fileName, e);
        }
    }

    // 2. ميثود لقراءة ملف JSON كخريطة بسيطة (Map<String, String>)
    public static Map<String, String> readAsMap(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON map from: " + fileName, e);
        }
    }

    // 3. ميثود لقراءة ملف JSON المتداخل (Map<String, Map<String, String>>)
    // مناسبة للهيكل الذي ربطنا فيه الـ href بالـ expectedText
    public static Map<String, Map<String, String>> readAsNestedMap(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read nested JSON file: " + fileName, e);
        }
    }

    public static List<Map<String, String>> readProducts(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // هيكل الـ JSON هو عبارة عن Map تحتوي على مفتاح "products" وقيمته قائمة
            Map<String, List<Map<String, String>>> wrapper = mapper.readValue(
                    new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, List<Map<String, String>>>>() {});
            return wrapper.get("products");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read products list", e);
        }
    }
}