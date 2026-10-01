package test;

import client.CourierClient;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.equalTo;

public class CourierDeleteTest {

    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void courierCanBeDeleted() {
        Courier courier = CourierGenerator.getRandomCourier();

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
                .statusCode(200);

        int courierId = loginResponse.path("id");

        courierClient.delete(courierId)
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    public void courierCannotBeDeletedWithoutId() {
        courierClient.deleteWithoutId()
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Not Found."));
    }

    @Test
    public void nonExistentCourierCannotBeDeleted() {
        int nonExistentCourierId = Integer.MAX_VALUE;

        courierClient.delete(nonExistentCourierId)
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("Курьера с таким id нет."));
    }
}