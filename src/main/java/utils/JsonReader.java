package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    @DataProvider(name = "LoginData")
    public static Object[][] getLoginData() {
        List<Map<String, String>> dataList = readAsList("loginData.json");
        Object[][] data = new Object[dataList.size()][3];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).get("email");
            data[i][1] = dataList.get(i).get("password");
            data[i][2] = dataList.get(i).get("scenarioType");
        }
        return data;
    }

    public static List<Map<String, String>> readAsList(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<List<Map<String, String>>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON list from: " + fileName, e);
        }
    }

    public static Map<String, String> readAsMap(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON map from: " + fileName, e);
        }
    }

    public static Map<String, Map<String, String>> readAsNestedMap(String fileName) {
        try {
            return mapper.readValue(new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read nested JSON file: " + fileName, e);
        }
    }

    public static List<Map<String, String>> readProducts(String fileName) {
        try {
            Map<String, List<Map<String, String>>> wrapper = mapper.readValue(
                    new File("src/test/resources/" + fileName),
                    new TypeReference<Map<String, List<Map<String, String>>>>() {});
            return wrapper.get("products");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read products list", e);
        }
    }
}