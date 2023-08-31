package org.poolc.api.badge;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoBadgeGiveTest extends AcceptanceTest {

    @Autowired
    BadgeDataController badgeDataController;

    @BeforeAll
    public void beforeAll(){
        badgeDataController.dataInitial();
    }

    @BeforeEach
    public void before(){
        badgeDataController.dataReset();
    }

    @Test
    public void 로그인_뱃지_확인(){
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getMyBadge(accessToken);
        assertThat(response.body().jsonPath().getInt("data[0].id")).isEqualTo(1L);
    }

    public static ExtractableResponse<Response> getAllBadge(String accessToken){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/badge/all")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getMyBadge(String accessToken){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/badge")
                .then().log().all()
                .extract();
    }
}
