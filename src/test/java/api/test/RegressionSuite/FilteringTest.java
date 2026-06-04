package api.test.RegressionSuite;

import api.endpoints.UserEndpoint;
import api.validations.FilteringValidator;
import api.validations.StatusCodeValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class FilteringTest {

    @Test(priority = 0)
    public void validateActiveUsersFilter() {
        Response filteredResponse = UserEndpoint.readAllUsersByStatus("active");
        filteredResponse.then().log().all();
        StatusCodeValidator.validate(filteredResponse, 200, "HTTP/1.1 200 OK");
        FilteringValidator.getStringFilter(filteredResponse, "status", "active");

    }

    @Test(priority = 1)
    public void validateInactiveUsersFilter() {
        Response filteredResponse = UserEndpoint.readAllUsersByStatus("inactive");
        filteredResponse.then().log().all();
        StatusCodeValidator.validate(filteredResponse, 200, "HTTP/1.1 200 OK");
        FilteringValidator.getStringFilter(filteredResponse, "status", "inactive");

    }

    @Test(priority = 2)
    public void validateMaleUsersFilter() {
        Response filteredResponse = UserEndpoint.readAllUsersByGender("male");
        filteredResponse.then().log().all();
        StatusCodeValidator.validate(filteredResponse, 200, "HTTP/1.1 200 OK");
        FilteringValidator.getStringFilter(filteredResponse, "gender", "male");

    }

    @Test(priority = 3)
    public void validateFemaleUsersFilter() {
        Response filteredResponse = UserEndpoint.readAllUsersByGender("female");
        filteredResponse.then().log().all();
        StatusCodeValidator.validate(filteredResponse, 200, "HTTP/1.1 200 OK");
        FilteringValidator.getStringFilter(filteredResponse, "gender", "female");

    }
}
