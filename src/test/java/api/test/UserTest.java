package api.test;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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

        Response response = UserEndpoint.readUser(createdID);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");

        logger.info("**** User id displayed ***");
    }

    @Test(priority = 3)
    public void updateUserTest() {

        logger.info("**** Updating a User ***");
        //data to be updated
        userPayload.setName(fk.name().name());
        userPayload.setEmail(fk.internet().emailAddress());

        Response response = UserEndpoint.updateUser(createdID, userPayload);
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


}


