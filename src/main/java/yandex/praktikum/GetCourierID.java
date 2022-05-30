package yandex.praktikum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetCourierID {

    public int getId(ProfileCourierData profile) {

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(profile.LoginInfo())
                .post("/api/v1/courier/login");
        return response.body().path("id");
    }
}
