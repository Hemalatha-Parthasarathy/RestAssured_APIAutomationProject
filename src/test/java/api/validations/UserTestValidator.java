package api.validations;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.empty;

public class UserTestValidator {

    public static int createNewUser() {
        Faker fk = new Faker();
        UserPayload userPayload = new UserPayload();

        userPayload.setName(fk.name().name());
        userPayload.setEmail(fk.internet().emailAddress());
        userPayload.setGender("female");
        userPayload.setStatus("active");

        Response response = UserEndpoint.createUser(userPayload);
        response.then().log().all();
        StatusCodeValidator.validate(response, 201, "HTTP/1.1 201 Created");
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        int createdID = response.jsonPath().getInt("id");
        return createdID;
    }

    public static Response getUsersResponse() {
        Response getUserResponse = UserEndpoint.readAllUsers();
        getUserResponse.then().log().all();
        StatusCodeValidator.validate(getUserResponse, 200, "HTTP/1.1 200 OK");
        // headers
        HeaderValidator.contentTypeValidate(getUserResponse, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(getUserResponse, 60);
        //Schema validation
        SchemaValidator.schemaValidator(getUserResponse, "schemas/user_schema.json");
        return getUserResponse;
    }

    public static List<Response> getUsersAllPagesResponse() {

        List<Response> responses = new ArrayList<>();
        Response getPage1Response = UserEndpoint.readAllUsersByPages(1);
        getPage1Response.then().log().all();
        int totalPage = Integer.parseInt(getPage1Response.getHeader("X-Pagination-Total"));

        responses.add(getPage1Response);

        for (int pageNO = 2; pageNO <= totalPage; pageNO++) {
            Response getPageResponse = UserEndpoint.readAllUsersByPages(pageNO);
            getPageResponse.then().log().all();
            List<Object> responseData = getPageResponse.jsonPath().getList("$");
            if (responseData.isEmpty()) {
                break;
            } else {
                responses.add(getPageResponse);
            }
        }
        return responses;
    }

    public static Response getUserResponse(int id) {
        Response getUserResponse = UserEndpoint.readSingleUser(id);
        getUserResponse.then().log().all();
        StatusCodeValidator.validate(getUserResponse, 200, "HTTP/1.1 200 OK");
        // headers
        HeaderValidator.contentTypeValidate(getUserResponse, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(getUserResponse, 60);
        //Schema validation
        SchemaValidator.schemaValidator(getUserResponse, "schemas/user_schema.json");
        return getUserResponse;
    }

    public static Response getdeletedResponse(int id) {
        Response getUserResponse = UserEndpoint.readSingleUser(id);
        getUserResponse.then().log().all();
        StatusCodeValidator.validate(getUserResponse, 404, "HTTP/1.1 404 Not Found");
        return getUserResponse;

    }
}