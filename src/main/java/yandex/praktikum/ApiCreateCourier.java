package yandex.praktikum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiCreateCourier {

    public Response newCourier(ProfileCourierData profile) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(profile)
                .post("/api/v1/courier");
    }
}
