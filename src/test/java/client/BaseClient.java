package client;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseClient {

    protected static final String BASE_URL =
            "https://qa-scooter.praktikum-services.ru";

    protected RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json");
    }
}