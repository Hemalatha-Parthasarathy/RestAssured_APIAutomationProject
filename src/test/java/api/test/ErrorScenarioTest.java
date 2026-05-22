package api.test;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorScenarioTest {

    UserPayload userPayload;


    @Test
    public void incorrectFieldvaluesTest() {
        userPayload = new UserPayload();
        userPayload.setName("Peter");
        userPayload.setGender("male4");
        userPayload.setEmail("123abc@gmail.com");
        userPayload.setStatus("active5");

        Response response = UserEndpoint.createUser(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 422);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 422 Unprocessable Entity");
    }

    @Test
    public void missingTokenTest() {
        userPayload = new UserPayload();
        userPayload.setName("PeterEngland");
        userPayload.setGender("male");
        userPayload.setEmail("abc12345@gmail.com");
        userPayload.setStatus("active");

        Response response = UserEndpoint.createUserMissedToken(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 401 Unauthorized");
        Assert.assertEquals(response.jsonPath().getString("message"), "Authentication failed. Please provide Authorization: Bearer <token> header.");
    }

    @Test
    public void invalidTokenTest() {
        userPayload = new UserPayload();
        userPayload.setName("PeterEngland");
        userPayload.setGender("male");
        userPayload.setEmail("abc1234567@gmail.com");
        userPayload.setStatus("active");

        Response response = UserEndpoint.createUserInvalidToken(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 403 Forbidden");
        Assert.assertEquals(response.jsonPath().getString("message"), "Forbidden. This token does not have permission to access this endpoint.");
    }

    @Test
    public void unknownIDTest() {
        userPayload = new UserPayload();

        Response response = UserEndpoint.readUser(123);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("message"), "Resource not found");
    }

    @Test
    public void noContentTypeTest() {
        userPayload = new UserPayload();
        userPayload.setName("Peterley");
        userPayload.setGender("male");
        userPayload.setEmail("1234abc@gmail.com");
        userPayload.setStatus("active");

        Response response = UserEndpoint.createUserNoContentType(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 415);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 415 Unsupported Media Type");
        Assert.assertEquals(response.jsonPath().getString("message"), "Unsupported media type. Please send Content-Type: application/json");
    }

    @Test
    public void invalidJsonTest() {

        Response response = UserEndpoint.createUserInvalidJSON();
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("message"), "Bad request. Invalid JSON in request body.");
    }

    @Test
    public void methodInvalidTest() {
        userPayload = new UserPayload();
        userPayload.setName("Peter");
        userPayload.setGender("male");
        userPayload.setEmail("1236abc@gmail.com");
        userPayload.setStatus("active");

        Response response = UserEndpoint.createUserInvalidMethod(userPayload, 1001);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 405 Method Not Allowed");
        Assert.assertEquals(response.jsonPath().getString("message"), "Method POST not allowed on this endpoint. Allowed: GET, PUT, PATCH, DELETE");
    }
}
