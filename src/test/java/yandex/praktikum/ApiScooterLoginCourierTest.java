package yandex.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiScooterLoginCourierTest {

    ApiDeleteTestCourier deleteCourier;
    ApiCreateCourier createCourier;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        deleteCourier = new ApiDeleteTestCourier();
        createCourier = new ApiCreateCourier();
    }

    @Test
    @DisplayName("Позитивный тест авторизации")
    public void loginCorrectCourierAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        createCourier.newCourier(profile);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(200)
                .and().assertThat().body("id", notNullValue());

        deleteCourier.deleteTestCourier(profile);
    }

    @Test
    @DisplayName("Авторизация с невалидными данными")
    public void loginNonexistentCourierAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("NonexistentCourier69   ", "Nonexistentpassword");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с валидным логином, но без пароля")
    public void loginCorrectCourierWithoutPasswordAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        createCourier.newCourier(profile);

        profile.setPassword("");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        profile.setPassword("password");

        deleteCourier.deleteTestCourier(profile);

    }

    @Test
    @DisplayName("Авторизация без пароля, но с валидным логином")
    public void loginCorrectCourierWithoutLoginAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        createCourier.newCourier(profile);

        profile.setLogin("");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));

        profile.setLogin("KirillTest");

        deleteCourier.deleteTestCourier(profile);

    }

    @Test
    @DisplayName("Авторизация с валидным паролем,  в валидном логине не хватает символа")
    public void loginCourierWithIncorrectLoginAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        createCourier.newCourier(profile);

        profile.setLogin("KirillTes");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));

        profile.setLogin("KirillTest");

        deleteCourier.deleteTestCourier(profile);

    }

    @Test
    @DisplayName("Авторизация с валидным логином,  в валидном пароле не хватает символа")
    public void loginCourierWithIncorrectPasswordAndCheckResponse() {
        ProfileCourierData profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        createCourier.newCourier(profile);

        profile.setPassword("passwor");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        response.then().statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));

        profile.setPassword("password");

        deleteCourier.deleteTestCourier(profile);

    }
}
