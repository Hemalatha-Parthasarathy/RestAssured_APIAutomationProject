package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.payloads.UserPayload;
import api.utilities.DataProviders;
import api.utilities.ExcelUtilities;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class DataDrivenTest {

    String path = System.getProperty("user.dir") + "//TestData//TestData.xlsx";
    ExcelUtilities xl = new ExcelUtilities(path);
    UserPayload userPayload;

    @Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class)
    public void createUserTest(int rowCount, String name, String email, String gender, String status) throws IOException {

        userPayload = new UserPayload();
        userPayload.setName(name);
        userPayload.setEmail(email);
        userPayload.setGender(gender);
        userPayload.setStatus(status);


        Response response = UserEndpoint.createUser(userPayload);
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertEquals(response.getContentType(), "application/json; charset=utf-8");
        String createdId = response.jsonPath().getString("id");

        xl.setCellData("Sheet1", rowCount, 4, createdId);

    }

    @Test(priority = 2, dataProvider = "Id", dataProviderClass = DataProviders.class)
    public void deleteUserTest(String id) {
        userPayload = new UserPayload();

        Response response = UserEndpoint.deleteUser(Integer.parseInt(id));
        response.then().log().all();

        Assert.assertEquals(response.getStatusCode(), 204);
    }


}
