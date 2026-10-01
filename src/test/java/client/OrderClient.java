package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";
    private static final String ORDER_BY_TRACK_PATH = "/api/v1/orders/track";
    private static final String ACCEPT_ORDER_PATH = "/api/v1/orders/accept";

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

    @Step("Получение заказа по номеру track = {track}")
    public Response getOrderByTrack(int track) {
        return getRequestSpecification()
                .queryParam("t", track)
                .when()
                .get(ORDER_BY_TRACK_PATH);
    }

    @Step("Получение заказа без номера track")
    public Response getOrderWithoutTrack() {
        return getRequestSpecification()
                .when()
                .get(ORDER_BY_TRACK_PATH);
    }

    @Step("Принятие заказа с id = {orderId} курьером с id = {courierId}")
    public Response acceptOrder(int orderId, int courierId) {
        return getRequestSpecification()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принятие заказа без id курьера")
    public Response acceptOrderWithoutCourierId(int orderId) {
        return getRequestSpecification()
                .when()
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принятие заказа без id заказа курьером с id = {courierId}")
    public Response acceptOrderWithoutOrderId(int courierId) {
        return getRequestSpecification()
                .queryParam("courierId", courierId)
                .when()
                .put(ACCEPT_ORDER_PATH + "/");
    }
}