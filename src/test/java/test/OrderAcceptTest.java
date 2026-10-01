package test;

import client.CourierClient;
import client.OrderClient;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;
import utils.OrderGenerator;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderAcceptTest {

    private CourierClient courierClient;
    private OrderClient orderClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        orderClient = new OrderClient();

        courier = CourierGenerator.getRandomCourier();

        courierClient.create(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );

        Response loginResponse = courierClient.login(credentials);

        loginResponse.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = loginResponse.path("id");
    }

    @Test
    public void courierCanAcceptOrder() {
        int orderId = createOrderAndGetId();

        orderClient.acceptOrder(orderId, courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void orderCannotBeAcceptedWithoutCourierId() {
        int orderId = createOrderAndGetId();

        orderClient.acceptOrderWithoutCourierId(orderId)
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    public void orderCannotBeAcceptedWithNonExistentCourierId() {
        int orderId = createOrderAndGetId();
        int nonExistentCourierId = Integer.MAX_VALUE;

        orderClient.acceptOrder(orderId, nonExistentCourierId)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    public void orderCannotBeAcceptedWithoutOrderId() {
        orderClient.acceptOrderWithoutOrderId(courierId)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Not Found."));
    }

    @Test
    public void nonExistentOrderCannotBeAccepted() {
        int nonExistentOrderId = Integer.MAX_VALUE;

        orderClient.acceptOrder(nonExistentOrderId, courierId)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    private int createOrderAndGetId() {
        Order order = OrderGenerator.getOrder(Arrays.asList("BLACK"));

        Response createResponse = orderClient.create(order);

        createResponse.then()
                .statusCode(201)
                .body("track", notNullValue());

        int track = createResponse.path("track");

        Response getOrderResponse = orderClient.getOrderByTrack(track);

        getOrderResponse.then()
                .statusCode(200)
                .body("order.id", notNullValue());

        return getOrderResponse.path("order.id");
    }

    @After
    public void tearDown() {
        if (courierId != null) {
            courierClient.delete(courierId);
        }
    }
}