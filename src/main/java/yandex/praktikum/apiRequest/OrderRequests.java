package yandex.praktikum.apiRequest;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import yandex.praktikum.apiData.OrderData;
import yandex.praktikum.apiData.ProfileCourierData;

import static io.restassured.RestAssured.given;

public class OrderRequests {

    @Step("Отменяем заказ")
    public int cancelOrder(int id) {
        Response response =
                given()
                        .param("track", id)
                        .put("/api/v1/orders/cancel");
        return response.statusCode();
    }

    @Step("Создание заказа")
    public Response createOrder(OrderData order) {
        return  given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response getOrderList() {
        return  given()
                .get("/api/v1/orders");
    }
}
