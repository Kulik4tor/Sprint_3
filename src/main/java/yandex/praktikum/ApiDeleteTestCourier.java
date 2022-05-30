package yandex.praktikum;

import static io.restassured.RestAssured.given;

public class ApiDeleteTestCourier {
    public void deleteTestCourier(ProfileCourierData profile) {
        GetCourierID courierID = new GetCourierID();
        int id = courierID.getId(profile);
        given()
                .delete("/api/v1/courier/" + id)
                .then().statusCode(200);
    }
}
