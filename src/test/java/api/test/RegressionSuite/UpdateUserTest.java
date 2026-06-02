package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import api.validations.HeaderValidator;
import api.validations.SchemaValidator;
import api.validations.StatusCodeValidator;
import api.validations.UserTestValidator;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class UpdateUserTest {
    public Logger logger;
    UserPayload updatedPayload;
    UserPayload partialUpdatePayload;
    int createdID;
    Faker fk;

    @BeforeTest
    public void setUp() {
        //logs
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void updatePUTUserTest() {
        fk = new Faker();
        updatedPayload = new UserPayload();

        createdID = UserTestValidator.createNewUser();
        logger.info("**** Created a User with ID ***");

        //data to be update
        updatedPayload.setName(fk.name().name());
        updatedPayload.setEmail(fk.internet().emailAddress());
        updatedPayload.setGender("male");
        updatedPayload.setStatus("inactive");

        //update call after post the data using generated ID
        Response updatePutResponse = UserEndpoint.updatePUTUser(createdID, updatedPayload);
        updatePutResponse.then().log().all();
        StatusCodeValidator.validate(updatePutResponse, 200, "HTTP/1.1 200 OK");
        logger.info("*** Data updated with status code is 200  - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(updatePutResponse, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(updatePutResponse, 60);
        logger.info("*** Updated data headers are validated- passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(updatePutResponse, "schemas/user_schema.json");
        logger.info("*** Updated data schema are validated- passed ***");

        //response body against the Updatedrequest
        updatePutResponse.then()
                .body("name", equalTo(updatedPayload.getName()))
                .body("email", equalTo(updatedPayload.getEmail()))
                .body("gender", equalTo(updatedPayload.getGender()))
                .body("status", equalTo(updatedPayload.getStatus()))
                .body("id", equalTo(createdID));
        logger.info("*** Updated data are validated against request data- passed ***");

//*****Get the updated details****/
        UserTestValidator.getUserResponse(createdID);
        logger.info("*** Updated data are validated through GET Call - passed ***");
    }

    @Test(priority = 2)
    public void updatePATCHUserTest() {
        fk = new Faker();
        partialUpdatePayload = new UserPayload();

        createdID = UserTestValidator.createNewUser();
        Response getResponse = UserTestValidator.getUserResponse(createdID);
        //Deserialize the POJO class
        partialUpdatePayload = getResponse.as(UserPayload.class);

        logger.info("**** Created a User with ID ***");

        //data to be updated
        partialUpdatePayload.setName(fk.name().name());
        partialUpdatePayload.setStatus("inactive");

        //update call after post the data using generated ID
        Response updatePatchResponse = UserEndpoint.updatePATCHUser(createdID, partialUpdatePayload);
        updatePatchResponse.then().log().all();
        StatusCodeValidator.validate(updatePatchResponse, 200, "HTTP/1.1 200 OK");
        logger.info("*** Data updated with status code is 200  - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(updatePatchResponse, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(updatePatchResponse, 60);
        logger.info("*** Updated data headers are validated- passed ***");

        //Schema validation
        SchemaValidator.schemaValidator(updatePatchResponse, "schemas/user_schema.json");
        logger.info("*** Updated data schema are validated- passed ***");

        //response body against the Updatedrequest
        updatePatchResponse.then()
                .body("name", equalTo(partialUpdatePayload.getName()))
                .body("email", equalTo(partialUpdatePayload.getEmail()))
                .body("gender", equalTo(partialUpdatePayload.getGender()))
                .body("status", equalTo(partialUpdatePayload.getStatus()))
                .body("id", equalTo(createdID));
        logger.info("*** Updated data are validated against request data- passed ***");

//*****Get the updated details****/
        UserTestValidator.getUserResponse(createdID);
        logger.info("*** Updated data are validated through GET Call - passed ***");
    }
}

