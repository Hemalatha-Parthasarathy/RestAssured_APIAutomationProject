package api.validations;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaValidator {

    public static void schemaValidator(Response response, String schema) {
        response.then().body(matchesJsonSchemaInClasspath(schema));
    }
}
