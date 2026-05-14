package APIValidations;

import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class HttpRequest {
    int id;

   /* @Test (priority = 0)
    void getAllUser(){
        given()
                .when().get("https://fakestoreapi.com/products")
                .then().statusCode(200).log().all();
    }*/

    @Test (priority =1)
    void getSingleUser(){
        given().when().get("https://fakestoreapi.com/products/1")
                .then().statusCode(200).log().all();
    }

    @Test (priority =2)
    void createUser(){
        HashMap data = new HashMap();
        data.put("title","Leather Shoe");
        data.put("price","28.5");
        data.put("description","this is the new feature included");
        data.put("category","Shoe");

        id = given().contentType("application/json").body(data)
                .when().post("https://fakestoreapi.com/products").jsonPath().getInt("id");
               // .then().statusCode(201).log().all();

    }


   @Test (priority =3,dependsOnMethods = {"createUser"})
    void updateUser(){
        HashMap data1 = new HashMap();
       data1.put("title","Leather shirt");
       data1.put("price","28.5");
       data1.put("description","this is the new feature included");
       data1.put("category","shirt");

        given().contentType("application/json").body(data1)
                .when().put("https://fakestoreapi.com/products/"+id)
                .then().statusCode(200).log().all();

    }

}
