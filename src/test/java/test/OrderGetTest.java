package test;

import client.OrderClient;
import io.restassured.response.Response;
import model.Order;
import org.junit.Before;
import org.junit.Test;
import utils.OrderGenerator;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderGetTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void orderCanBeReceivedByTrack() {
        Order order = OrderGenerator.getOrder(Arrays.asList("BLACK"));

        Response createResponse = orderClient.create(order);

        createResponse.then()
                .statusCode(201)
                .body("track", notNullValue());

        int track = createResponse.path("track");

        orderClient.getOrderByTrack(track)
                .then()
                .statusCode(200)
                .body("order", notNullValue())
                .body("order.track", equalTo(track));
    }

    @Test
    public void orderCannotBeReceivedWithoutTrack() {
        orderClient.getOrderWithoutTrack()
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void nonExistentOrderCannotBeReceived() {
        int nonExistentTrack = Integer.MAX_VALUE;

        orderClient.getOrderByTrack(nonExistentTrack)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Заказ не найден"));
    }
}