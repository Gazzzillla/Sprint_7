package test;

import client.CourierClient;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void courierCanBeCreated() {
        courier = CourierGenerator.getRandomCourier();

        Response response = courierClient.create(courier);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    public void identicalCouriersCannotBeCreated() {
        courier = CourierGenerator.getRandomCourier();

        courierClient.create(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierClient.create(courier)
                .then()
                .statusCode(409)
                .body("message",
                        equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void courierWithoutLoginCannotBeCreated() {
        courier = CourierGenerator.getCourierWithoutLogin();

        courierClient.create(courier)
                .then()
                .statusCode(400)
                .body("message",
                        equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void courierWithoutPasswordCannotBeCreated() {
        courier = CourierGenerator.getCourierWithoutPassword();

        courierClient.create(courier)
                .then()
                .statusCode(400)
                .body("message",
                        equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courier == null
                || courier.getLogin() == null
                || courier.getPassword() == null) {
            return;
        }

        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );

        Response loginResponse = courierClient.login(credentials);

        if (loginResponse.statusCode() == 200) {
            int courierId = loginResponse.path("id");

            courierClient.delete(courierId)
                    .then()
                    .statusCode(200)
                    .body("ok", equalTo(true));
        }
    }
}