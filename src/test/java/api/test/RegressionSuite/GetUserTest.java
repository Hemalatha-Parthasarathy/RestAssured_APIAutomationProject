package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.validations.HeaderValidator;
import api.validations.SchemaValidator;
import api.validations.StatusCodeValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GetUserTest {
    public Logger logger;

    @BeforeTest
    public void setUp() {
        //logs
        logger = LogManager.getLogger(this.getClass());

    }

    @Test(priority = 1)
    public void getUserTest() {
        logger.info("**** Reading a User ***");

        Response response = UserEndpoint.readSingleUser(1001);
        response.then().log().all();

        //Validations
        // status code
        StatusCodeValidator.validate(response, 200, "HTTP/1.1 200 OK");
        logger.info("*** Expected status code is 200 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(response, 60);
        logger.info("*** Expected headers are validated- passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(response, "schemas/user_schema.json");
        logger.info("*** Expected schema are validated- passed ***");

    }

    @Test(priority = 2)
    public void getAllUsersTest() {
        logger.info("**** Reading all Users ***");

        Response response = UserEndpoint.readAllUsers();
        response.then().log().all();

        //Validations
        // status code
        StatusCodeValidator.validate(response, 200, "HTTP/1.1 200 OK");
        logger.info("*** Expected status code is 200 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(response, 60);
        HeaderValidator.paginationValidate(response, 20);
        logger.info("*** Expected headers are validated - passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(response, "schemas/users_schema.json");
        logger.info("*** Expected schema are validated - passed ***");

    }
}
