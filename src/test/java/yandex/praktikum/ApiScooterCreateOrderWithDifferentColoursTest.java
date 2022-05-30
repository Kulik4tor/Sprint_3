package yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class ApiScooterCreateOrderWithDifferentColoursTest {

    public String[] colour;
    ApiCancelTestOrder cancelTestOrder;

    public ApiScooterCreateOrderWithDifferentColoursTest(String[] colour) {
        this.colour = colour;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        cancelTestOrder = new ApiCancelTestOrder();
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
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

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .post("/api/v1/orders");
        response.then().statusCode(201)
                .and().assertThat().body("track", notNullValue());

        if (cancelTestOrder.cancelOrder(response.body().path("track")) != 200) {
            Assert.fail("Не удалось удалить тестовый заказ id = " + response.body().asString());
        }
    }
}
