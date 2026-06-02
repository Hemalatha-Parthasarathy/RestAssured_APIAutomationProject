package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import api.validations.HeaderValidator;
import api.validations.StatusCodeValidator;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.notNullValue;

public class UserTest {
    public Logger logger;
    Faker fk;
    UserPayload userPayload;
    int createdID;

    @BeforeTest
    public void setUp() {
        fk = new Faker();
        userPayload = new UserPayload();
        userPayload.setName(fk.name().name());
        userPayload.setEmail(fk.internet().emailAddress());
        userPayload.setGender(Math.random() > 0.5 ? "male" : "female");
        userPayload.setStatus("active");

        //logs
        logger = LogManager.getLogger(this.getClass());

    }

    @Test(priority = 1)
    public void createUserTest() {
        logger.info("**** Create a User ***");
        Response response = UserEndpoint.createUser(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");
        createdID = response.jsonPath().getInt("id");
        logger.info("**** Created a User with ID ***");
    }

    @Test(priority = 2)
    public void getUserTest() {

        logger.info("**** Reading a User ***");

        Response response = UserEndpoint.readSingleUser(1001);
        response.then().log().all();

        //Validations - status code
        StatusCodeValidator.validate(response, 200, "HTTP/1.1 200 OK");
        logger.info("*** Expected status code is 200 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(response, 60);

        //response body
        JsonPath json = response.jsonPath();

        Assert.assertTrue(json.get("id") instanceof Integer);
        Assert.assertTrue(json.get("name") instanceof String);
        Assert.assertTrue(json.get("email") instanceof String);
        Assert.assertTrue(json.get("gender") instanceof String);
        Assert.assertTrue(json.get("status") instanceof String);


        logger.info("**** User id displayed ***");
    }

    @Test(priority = 3)
    public void updateUserTest() {

        logger.info("**** Updating a User ***");
        //data to be updated
        userPayload.setName(fk.name().name());
        userPayload.setEmail(fk.internet().emailAddress());

        Response response = UserEndpoint.updatePATCHUser(createdID, userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");
        logger.info("**** Updated a User ***");
    }

    @Test(priority = 4)
    public void deleteUserTest() {

        logger.info("**** Deleting a User ***");
        Response response = UserEndpoint.deleteUser(createdID);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 204);


        logger.info("**** User is deleted ***");
    }

    //Data type validation for the Json Fields
    @Test(priority = 5)
    public void fieldTypeValidation() {
        Response response = UserEndpoint.readSingleUser(createdID);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");

        logger.info("**** User id displayed ***");
    }

    @Test(priority = 6)
    public void getAllUsersTest() {

        logger.info("**** Reading a User ***");

        Response response = UserEndpoint.readAllUsers();
        response.then().log().all();

        //Validations - status code
        StatusCodeValidator.validate(response, 200, "HTTP/1.1 200 OK");
        logger.info("*** Expected status code is 200 - passed ***");

        // headers
        HeaderValidator.contentTypeValidate(response, "application/json; charset=utf-8");
        HeaderValidator.rateLimitValidate(response, 60);
        HeaderValidator.paginationValidate(response, 20);

        //response body
        JsonPath json = response.jsonPath();

        Assert.assertTrue(json.get("id") instanceof Integer);
        Assert.assertTrue(json.get("name") instanceof String);
        Assert.assertTrue(json.get("email") instanceof String);
        Assert.assertTrue(json.get("gender") instanceof String);
        Assert.assertTrue(json.get("status") instanceof String);


        logger.info("**** User id displayed ***");
    }

}


