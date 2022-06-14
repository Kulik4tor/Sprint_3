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

public class ApiScooterCreateCourierTest {

    CourierRequests courierRequests;
    boolean needDeleteCreatedCourier;
    ProfileCourierData profile;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierRequests = new CourierRequests();
        needDeleteCreatedCourier = false;
    }


    @Test
    @DisplayName("Позитивный тест создания Курьера")
    public void CreateCourierCorrectStatusCodeTest() {

        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        Response response = courierRequests.createCourier(profile);
        if (response.statusCode() == SC_CREATED) {
            needDeleteCreatedCourier = true;
        }
        response.then().statusCode(SC_CREATED)
                .and().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void CreateCourierWithoutLoginTest() {

        profile = new ProfileCourierData(null, "password", "Kirill");
        Response response = courierRequests.createCourier(profile);
        if (response.statusCode() == SC_CREATED) {
            needDeleteCreatedCourier = true;
        }
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void CreateCourierWithoutPasswordTest() {

        profile = new ProfileCourierData("KirillTest", null, "Kirill");
        Response response = courierRequests.createCourier(profile);
        if (response.statusCode() == SC_CREATED) {
            needDeleteCreatedCourier = true;
        }
        response.then().statusCode(SC_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void CreateCourierWithoutNameTest() {

        profile = new ProfileCourierData("KirillTest", "password", null);
        Response response = courierRequests.createCourier(profile);
        if (response.statusCode() == SC_CREATED) {
            needDeleteCreatedCourier = true;
        }
        response.then().statusCode(SC_CREATED)
                .and().assertThat().body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void CreateAlreadyExistingCourierTest() {

        profile = new ProfileCourierData("KirillTest", "password", "Kirill");
        Response response = courierRequests.createCourier(profile);
        if (response.statusCode() == SC_CREATED) {
            needDeleteCreatedCourier = true;
        }
        response.then().statusCode(SC_CREATED)
                .and().assertThat().body("ok", equalTo(true));

        response = courierRequests.createCourier(profile);
        response.then().statusCode(SC_CONFLICT)
                .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @After
    public void tearDown() {
        if (needDeleteCreatedCourier) {
            courierRequests.deleteCourier(profile);
        }
    }
}
