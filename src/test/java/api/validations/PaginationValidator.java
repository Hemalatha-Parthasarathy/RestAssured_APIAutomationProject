package api.validations;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaginationValidator {

    public static void validatePageNumber(Response response, int expectedPageNo) {
        Assert.assertEquals(response.getHeader("X-Pagination-Page"), String.valueOf(expectedPageNo),
                "Incorrect Page Number");
    }

    public static void validatePerPageSize(Response response, int expectedLimit) {
        List<Object> users = response.jsonPath().getList("$");
        Assert.assertTrue(users.size() <= expectedLimit, "Exceeded the page limit");
    }

    public static void validateTotalPages(Response response) {

        int totalPages = Integer.parseInt(response.getHeader("X-Pagination-Pages"));
        Assert.assertTrue(totalPages > 0, "Total Pages should be greater than 0");
    }

    public static void validateEmptyPage(Response response) {
        List<Object> users = response.jsonPath().getList("$");
        Assert.assertTrue(users.isEmpty(), "Expected Empty Page");
    }

    public static void validateLastPage(Response response) {
        int currentPage = Integer.parseInt(response.getHeader("X-Pagination-Page"));
        int totalPages = Integer.parseInt(response.getHeader("X-Pagination-Pages"));
        Assert.assertEquals(currentPage, totalPages, "Not the last page");
    }

    public static void validateNoDuplicateRecords(List<Response> responses) {
        Set<Integer> uniqueIds = new HashSet<>();

        for (Response response : responses) {
            List<Integer> ids = response.jsonPath().getList("id");

            for (Integer id : ids) {
                Assert.assertTrue(uniqueIds.add(id), "Duplicate Ids found" + id);
            }
        }
    }
}
