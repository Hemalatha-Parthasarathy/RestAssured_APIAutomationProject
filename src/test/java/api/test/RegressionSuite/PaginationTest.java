package api.test.RegressionSuite;

import api.validations.PaginationValidator;
import api.validations.UserTestValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PaginationTest {

    @Test(priority = 0)
    public void validateCurrentPageNumber() {
        Response response = UserTestValidator.getUsersResponse();
        int currentPage = Integer.parseInt(response.getHeader("X-Pagination-Page"));
        PaginationValidator.validatePageNumber(response, currentPage);
    }


    @Test(priority = 1)
    public void validatePageNumber() {
        Response response = UserTestValidator.getUsersResponse();
        PaginationValidator.validatePageNumber(response, 2);
    }

    @Test(priority = 2)
    public void validatePagesize() {
        Response response = UserTestValidator.getUsersResponse();
        PaginationValidator.validatePerPageSize(response, 10);
    }

    @Test(priority = 3)
    public void validateTotalPages() {
        Response response = UserTestValidator.getUsersResponse();
        PaginationValidator.validateTotalPages(response);
    }

    @Test(priority = 4)
    public void validateLastPage() {
        Response response = UserTestValidator.getUsersResponse();
        PaginationValidator.validateLastPage(response);
    }

    @Test(priority = 5)
    public void validateEmptyPage() {
        Response response = UserTestValidator.getUsersResponse();
        PaginationValidator.validateEmptyPage(response);
    }

    @Test(priority = 6)
    public void validateNoDuplicateRecordsInPage() {
        List<Response> responses = UserTestValidator.getUsersAllPagesResponse();
        PaginationValidator.validateNoDuplicateRecords(responses);
    }

}
