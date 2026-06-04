package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.validations.HeaderValidator;
import api.validations.StatusCodeValidator;
import api.validations.UserTestValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DeleteUserTest {
    int createdId;
    Logger logger;

    @BeforeTest
    public void setUp() {
        logger = LogManager.getLogger(this.getClass());
    }

    @Test(priority = 1)
    public void deleteUser() {
        //create a user
        logger.info("****Created a User***");
        createdId = UserTestValidator.createNewUser();

        //get the created user
        logger.info("****Get the created User***");
        Response getResponse = UserTestValidator.getUserResponse(createdId);
        getResponse.then().log().all();

        //delete the created user
        logger.info("****Delete the created User***");
        Response deleteResponse = UserEndpoint.deleteUser(createdId);
        deleteResponse.then().log().all();

        //Validations
        // status code
        StatusCodeValidator.validate(deleteResponse, 204, "HTTP/1.1 204 No Content");
        logger.info("*** Expected status code is 204 deleted with No content - passed ***");

        //get the deleted User
        logger.info("****Get the deleted User***");
        Response getdeletedResponse = UserTestValidator.getdeletedResponse(createdId);
        getdeletedResponse.then().log().all();

        // status code
        StatusCodeValidator.validate(getdeletedResponse, 404, "HTTP/1.1 404 Not Found");
        logger.info("*** status code 404 stating not found - passed ***");
        Assert.assertEquals(getdeletedResponse.jsonPath().getString("message"), "Resource not found");
    }

}
