package api.endpoints;

import api.payloads.UserPayload;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.ResourceBundle;

import static io.restassured.RestAssured.*;

public class UserEndpoint {


    static ResourceBundle getURL() {
        ResourceBundle routes = ResourceBundle.getBundle("routes");
        return routes;
    }

    public static Response createUser(UserPayload payload) {

        String post_url = getURL().getString("post_url");
        String token = getURL().getString("token");

        Response response = given()
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .body(payload)
                .when().post(post_url);

        return response;
    }

    public static Response readUser(int id) {
        String get_url = getURL().getString("get_url");

        Response response = given()
                .pathParam("id", id)
                .when().get(get_url);
        return response;
    }

    public static Response updateUser(int id, UserPayload payload) {
        String update_url = getURL().getString("update_url");
        String token = getURL().getString("token");

        Response response = given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .pathParam("id", id)
                .body(payload)
                .when().put(update_url);

        return response;
    }

    public static Response deleteUser(int id) {
        String delete_url = getURL().getString("delete_url");
        String token = getURL().getString("token");


        Response response = given()
                .pathParam("id", id)
                .auth().oauth2(token)
                .when().delete(delete_url);
        return response;
    }


}
