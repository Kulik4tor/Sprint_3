package yandex.praktikum.apiCourier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex.praktikum.apiData.ProfileCourierData;
import yandex.praktikum.apiRequest.CourierRequests;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ApiScooterLoginCourierTest {

    CourierRequests courierRequests;
    ProfileCourierData profile;
    boolean needDeleteCreatedCourier;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierRequests = new CourierRequests();
        needDeleteCreatedCourier = false;
    }

    @Test
    @DisplayName("Позитивный тест авторизации")
    public void loginCorrectCourierAndCheckResponse() {
        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        courierRequests.createCourier(profile);
        needDeleteCreatedCourier = true;

        Response response = courierRequests.loginCourier(profile);
        response.then().statusCode(SC_OK)
                .and().assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с невалидными данными")
    public void loginNonexistentCourierAndCheckResponse() {
        profile = new ProfileCourierData("NonexistentCourier69   ", "Nonexistentpassword");
        needDeleteCreatedCourier = false;

        Response response = courierRequests.loginCourier(profile);
        response.then().statusCode(SC_NOT_FOUND)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с валидным логином, но без пароля")
    public void loginCorrectCourierWithoutPasswordAndCheckResponse() {
        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        courierRequests.createCourier(profile);
        needDeleteCreatedCourier = true;

        profile.setPassword("");

        Response response = courierRequests.loginCourier(profile);
        profile.setPassword("password");
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Авторизация без пароля, но с валидным логином")
    public void loginCorrectCourierWithoutLoginAndCheckResponse() {
        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        courierRequests.createCourier(profile);
        needDeleteCreatedCourier = true;

        profile.setLogin("");

        Response response = courierRequests.loginCourier(profile);
        profile.setLogin("KirillTest");
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с валидным паролем,  в валидном логине не хватает символа")
    public void loginCourierWithIncorrectLoginAndCheckResponse() {
        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        courierRequests.createCourier(profile);
        needDeleteCreatedCourier = true;

        profile.setLogin("KirillTes");

        Response response = courierRequests.loginCourier(profile);
        profile.setLogin("KirillTest");
        response.then().statusCode(SC_NOT_FOUND)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Авторизация с валидным логином,  в валидном пароле не хватает символа")
    public void loginCourierWithIncorrectPasswordAndCheckResponse() {
        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        courierRequests.createCourier(profile);
        needDeleteCreatedCourier = true;

        profile.setPassword("passwor");

        Response response = courierRequests.loginCourier(profile);
        profile.setPassword("password");
        response.then().statusCode(SC_NOT_FOUND)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        if (needDeleteCreatedCourier) {
            courierRequests.deleteCourier(profile);
        }
    }
}
