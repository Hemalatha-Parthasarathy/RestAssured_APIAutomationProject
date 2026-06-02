package api.validations;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import static org.hamcrest.Matchers.notNullValue;

public class HeaderValidator {

    public static void contentTypeValidate(Response response, String expectedContentType) {
        response.then().contentType(ContentType.JSON);
        String actualContentType = response.getContentType();
        Assert.assertTrue(actualContentType.contains(expectedContentType));
    }

    public static void rateLimitValidate(Response response, int rateLimit) {
        response.then().header("X-RateLimit-Limit", "60")
                .header("X-RateLimit-Remaining", notNullValue())
                .header("X-RateLimit-Reset", notNullValue());
    }

    public static void paginationValidate(Response response, int pageTotal) {
        response.then().header("X-Pagination-Total", "20")
                .header("X-Pagination-Pages", notNullValue())
                .header("X-Pagination-Page", notNullValue())
                .header("X-Pagination-Limit", notNullValue());
    }

}
