package api.validations;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class FilteringValidator {

    public static void getStringFilter(Response response, String fieldName, String expectedValue) {
        List<String> values = response.jsonPath().getList(fieldName);
        Assert.assertFalse(values.isEmpty(), "No records returned");

        for (String value : values) {
            Assert.assertEquals(value, expectedValue);
        }

    }
}

