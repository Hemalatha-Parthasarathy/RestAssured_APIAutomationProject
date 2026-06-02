package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import api.validations.HeaderValidator;
import api.validations.SchemaValidator;
import api.validations.StatusCodeValidator;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class CreateUserTest {
    public Logger logger;
    UserPayload userPayload;
    int createdID;
    Faker fk;

    @BeforeTest
    public void setUp() {
        fk = new Faker();
        userPayload = new UserPayload();
        userPayload.setName(fk.name().name());
        userPayload.setEmail(fk.internet().emailAddress());
        userPayload.setGender("female");
        userPayload.setStatus("active");

        //logs
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void createUserTest() {
        logger.info("**** Create a User ***");
        Response response = UserEndpoint.createUser(userPayload);
        response.then().log().all();

        //Validations
        // status code
        StatusCodeValidator.validate(response, 201, "HTTP/1.1 201 Created");
        logger.info("*** Expected status code is 201 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(response, 60);
        logger.info("*** Expected headers are validated - passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(response, "schemas/user_schema.json");
        logger.info("*** Expected schema are validated- passed ***");

        //response body against the request
        response.then()
                .body("name", equalTo(userPayload.getName()))
                .body("email", equalTo(userPayload.getEmail()))
                .body("gender", equalTo(userPayload.getGender()))
                .body("status", equalTo(userPayload.getStatus()))
                .body("id", greaterThan(0));

        createdID = response.jsonPath().getInt("id");
        logger.info("**** Created a User with ID ***");

        //Get call using generated ID
        Response getResponse = UserEndpoint.readSingleUser(createdID);
        response.then().log().all();
        StatusCodeValidator.validate(getResponse, 200, "HTTP/1.1 200 OK");
        logger.info("*** Expected status code is 200 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(getResponse, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(getResponse, 60);
        logger.info("*** Expected headers are validated- passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(getResponse, "schemas/user_schema.json");
        logger.info("*** Expected schema are validated- passed ***");


    }
}
