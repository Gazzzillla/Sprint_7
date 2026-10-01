package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response create(Order order) {
        return getRequestSpecification()
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return getRequestSpecification()
                .when()
                .get(ORDERS_PATH);
    }
}