package utils;

import model.Courier;

import java.util.UUID;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String randomValue = UUID.randomUUID().toString();

        return new Courier(
                "courier_" + randomValue,
                "123456",
                "Test"
        );
    }

    public static Courier getCourierWithoutLogin() {
        return new Courier(
                null,
                "123456",
                "Test"
        );
    }

    public static Courier getCourierWithoutPassword() {
        return new Courier(
                "courier_" + UUID.randomUUID(),
                null,
                "Test"
        );
    }
}