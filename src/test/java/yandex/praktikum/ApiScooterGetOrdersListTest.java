package yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiScooterGetOrdersListTest {
    public int testOrderTrack;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        OrderData order = new OrderData("Кирилл", "Кулик", "Москва", 4, "+7 800 355 35 35", 5, "2022-06-06", "Saske, come back to Konoha", new String[]{"BLACK"});

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post("/api/v1/orders");
        testOrderTrack = response.body().path("track");
    }

    @Test
    @DisplayName("Получение списка заказов (новые и в работе)")
    public void getOrdersList() {
        Response response =
                given()
                        .get("/api/v1/orders");

        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }

    @After
    public void quitTest() {
        ApiCancelTestOrder cancelTestOrder = new ApiCancelTestOrder();
        cancelTestOrder.cancelOrder(testOrderTrack);
    }
}
