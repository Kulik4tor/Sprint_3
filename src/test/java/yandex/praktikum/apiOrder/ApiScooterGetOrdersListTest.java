package yandex.praktikum.apiOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.praktikum.apiData.OrderData;
import yandex.praktikum.apiRequest.OrderRequests;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiScooterGetOrdersListTest {
    public int testOrderTrack;
    OrderRequests orderRequests = new OrderRequests();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        OrderData order = new OrderData("Кирилл", "Кулик", "Москва", 4, "+7 800 355 35 35", 5, "2022-06-06", "Saske, come back to Konoha", new String[]{"BLACK"});

        Response response = orderRequests.createOrder(order);
        testOrderTrack = response.body().path("track");
    }

    @Test
    @DisplayName("Получение списка заказов (новые и в работе)")
    public void getOrdersList() {
        Response response = orderRequests.getOrderList();

        response.then().assertThat().statusCode(SC_OK).and().body("orders", notNullValue());
    }

    @After
    public void quitTest() {
        orderRequests.cancelOrder(testOrderTrack);
    }
}
