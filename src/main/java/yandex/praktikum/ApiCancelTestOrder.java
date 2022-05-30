package yandex.praktikum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiCancelTestOrder {
    public int cancelOrder(int id) {
        Response response =
                given()
                        .param("track", id)
                        .put("/api/v1/orders/cancel");
        return response.statusCode();
    }
}
