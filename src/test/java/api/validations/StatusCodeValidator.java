package api.validations;

import io.restassured.response.Response;
import org.testng.Assert;

public class StatusCodeValidator {

    public static void validate(Response response, int expectedCode, String expectedStatusLine) {

        response.then().statusCode(expectedCode);
        String actualStatusLine = response.getStatusLine();
        Assert.assertTrue(actualStatusLine.contains(expectedStatusLine));
    }
}

