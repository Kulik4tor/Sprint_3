package yandex.praktikum.apiOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import yandex.praktikum.apiData.OrderData;
import yandex.praktikum.apiRequest.OrderRequests;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ApiScooterCreateOrderWithDifferentColoursTest {

    public String[] colour;
    OrderRequests orderRequests;

    public ApiScooterCreateOrderWithDifferentColoursTest(String[] colour) {
        this.colour = colour;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        orderRequests = new OrderRequests();
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getColoursForOrder() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}

        };
    }

    @Test
    @DisplayName("Создание заказа на самокат с разными цветами")
    public void createOrderWithDifferentColours() {

        OrderData order = new OrderData("Кирилл", "Кулик", "Москва", 4, "+7 800 355 35 35", 5, "2022-06-06", "Saske, come back to Konoha", colour);

        Response response = orderRequests.createOrder(order);
        response.then().statusCode(SC_CREATED)
                .and().assertThat().body("track", notNullValue());

        if (orderRequests.cancelOrder(response.body().path("track")) != SC_OK) {
            Assert.fail("Не удалось удалить тестовый заказ id = " + response.body().asString());
        }
    }
}
