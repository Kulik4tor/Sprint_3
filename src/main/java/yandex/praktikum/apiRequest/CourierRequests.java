package yandex.praktikum.apiRequest;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import yandex.praktikum.apiData.ProfileCourierData;

import static io.restassured.RestAssured.given;

public class CourierRequests {
    @Step("Получаем id курьера")
    public int getCourierId(ProfileCourierData profile) {

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        return response.body().path("id");
    }

    @Step("Удаляем тестового курьера")
    public void deleteCourier(ProfileCourierData profile) {
        int id = getCourierId(profile);
        given()
                .delete("/api/v1/courier/" + id)
                .then().statusCode(200);
    }

    @Step("Создаем курьера")
    public Response createCourier(ProfileCourierData profile) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(profile)
                .post("/api/v1/courier");
    }

    @Step("Авторизация курьера")
    public Response loginCourier(ProfileCourierData profile) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
    }
}
