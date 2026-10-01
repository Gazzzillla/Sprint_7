package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return getRequestSpecification()
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизация курьера")
    public Response login(CourierCredentials credentials) {
        return getRequestSpecification()
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера с id = {courierId}")
    public Response delete(int courierId) {
        return getRequestSpecification()
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}