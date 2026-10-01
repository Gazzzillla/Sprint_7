package test;

import client.CourierClient;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierGenerator;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();

        courierClient.create(courier)
                .then()
                .statusCode(201);
    }

    @Test
    public void courierCanLogin() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );

        Response response = courierClient.login(credentials);

        response.then()
                .statusCode(200)
                .body("id", greaterThan(0));

        courierId = response.path("id");
    }

    @Test
    public void courierCannotLoginWithoutLogin() {
        CourierCredentials credentials = new CourierCredentials(
                null,
                courier.getPassword()
        );

        courierClient.login(credentials)
                .then()
                .statusCode(400)
                .body("message",
                        equalTo("Недостаточно данных для входа"));
    }

    @Test
    public void courierCannotLoginWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                "wrong_password"
        );

        courierClient.login(credentials)
                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Учетная запись не найдена"));
    }

    @Test
    public void courierCannotLoginWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin() + "_wrong",
                courier.getPassword()
        );

        courierClient.login(credentials)
                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Учетная запись не найдена"));
    }

    @Test
    public void nonExistentCourierCannotLogin() {
        CourierCredentials credentials = new CourierCredentials(
                "nonexistent_" + UUID.randomUUID(),
                "123456"
        );

        courierClient.login(credentials)
                .then()
                .statusCode(404)
                .body("message",
                        equalTo("Учетная запись не найдена"));
    }

    @Test
    public void courierCannotLoginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials(
                courier.getLogin(),
                null
        );

        courierClient.login(credentials)
                .then()
                .statusCode(400)
                .body("message",
                        equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        if (courierId == null) {
            CourierCredentials credentials = new CourierCredentials(
                    courier.getLogin(),
                    courier.getPassword()
            );

            Response loginResponse = courierClient.login(credentials);

            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.path("id");
            }
        }

        if (courierId != null) {
            courierClient.delete(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}