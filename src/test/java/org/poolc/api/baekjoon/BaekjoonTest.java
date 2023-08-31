package org.poolc.api.baekjoon;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.badge.BadgeDataController;
import org.poolc.api.baekjoon.dto.PostBaekjoonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaekjoonTest extends AcceptanceTest  {

    @Autowired
    BaekjoonDataController baekjoonDataController;

    @BeforeAll
    public void beforeAll(){
        baekjoonDataController.dataInitial();
    }

    @BeforeEach
    public void before(){
        baekjoonDataController.dataReset();
    }

    @Test
    public void 내_모든_백준_조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getBaekjoon(accessToken);
        assertThat(response.body().jsonPath().getInt("data[0].problemId")).isEqualTo(1111L);
        assertThat(response.body().jsonPath().getString("data[0].date")).isEqualTo(LocalDate.now().toString());
    }

    @Test
    public void 문제풀기(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        PostBaekjoonRequest postBaekjoonRequest = PostBaekjoonRequest.builder()
                .language("java")
                .level(14L)
                .problemId(1234L)
                .problemTags(new ArrayList<>())
                .submissionId(1235L)
                .title("토마토2")
                .build();
        ExtractableResponse<Response> response = solveBaekjoon(accessToken, postBaekjoonRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response1 = getBaekjoon(accessToken);
        assertThat(response1.body().jsonPath().getInt("data[1].problemId")).isEqualTo(1234L);
    }

    @Test
    public void 문제_푼것_뱃지_반영(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        PostBaekjoonRequest postBaekjoonRequest = PostBaekjoonRequest.builder()
                .language("java")
                .level(14L)
                .problemId(1234L)
                .problemTags(new ArrayList<>())
                .submissionId(1235L)
                .title("토마토2")
                .build();
        solveBaekjoon(accessToken, postBaekjoonRequest);

        ExtractableResponse<Response> response2 = getAllBadge(accessToken);
        assertThat(response2.body().jsonPath().getInt("gold")).isEqualTo(1);
        assertThat(response2.body().jsonPath().getInt("baekjoon")).isEqualTo(1);
    }

    public static ExtractableResponse<Response> solveBaekjoon(String accessToken, PostBaekjoonRequest request){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/baekjoon")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getBaekjoon(String accessToken){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/baekjoon")
                .then().log().all()
                .extract();
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
}
