package api.validations;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;

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
}
