package yandex.praktikum;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ApiScooterCreateCourierTest {

    ApiDeleteTestCourier deleteCourier;
    ApiCreateCourier createCourier;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        deleteCourier = new ApiDeleteTestCourier();
        createCourier = new ApiCreateCourier();
    }


    @Test
    @DisplayName("Позитивный тест создания Курьера")
    public void CreateCourierCorrectStatusCodeTest() {

        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        Response response = createCourier.newCourier(profile);
        response.then().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));

        //удаление созданного курьера
        deleteCourier.deleteTestCourier(profile);
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void CreateCourierWithoutLoginTest() {

        ProfileCourierData profile = new ProfileCourierData(null, "password", "Kirill");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile)
                .post("/api/v1/courier");
        response.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void CreateCourierWithoutPasswordTest() {

        ProfileCourierData profile = new ProfileCourierData("KirillTest", null, "Kirill");
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile)
                .post("/api/v1/courier");
        response.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void CreateCourierWithoutNameTest() {

        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", null);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile)
                .post("/api/v1/courier");
        response.then().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));

        deleteCourier.deleteTestCourier(profile);
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void CreateAlreadyExistingCourierTest() {

        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        Response response = createCourier.newCourier(profile);
        response.then().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));

        response = createCourier.newCourier(profile);
        response.then().statusCode(409)
                .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        //удаление созданного курьера
        deleteCourier.deleteTestCourier(profile);
    }
}
