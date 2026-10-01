package utils;

import model.Order;

import java.time.LocalDate;
import java.util.List;

public class OrderGenerator {

    public static Order getOrder(List<String> color) {
        return new Order(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                LocalDate.now().plusDays(1).toString(),
                "Sprint 7 autotest",
                color
        );
    }
}