package test;

import client.OrderClient;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;

public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    public void ordersListIsReturned() {
        Response response = orderClient.getOrders();

        response.then()
                .statusCode(200)
                .body("orders", instanceOf(java.util.List.class));
    }
}