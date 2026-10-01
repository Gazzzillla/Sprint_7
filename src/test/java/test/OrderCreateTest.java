package test;

import client.OrderClient;
import io.restassured.response.Response;
import model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    public void orderCanBeCreatedWithDifferentColors() {
        Order order = OrderGenerator.getOrder(color);
        OrderClient orderClient = new OrderClient();

        Response response = orderClient.create(order);

        response.then()
                .statusCode(201)
                .body("track", greaterThan(0));
    }
}