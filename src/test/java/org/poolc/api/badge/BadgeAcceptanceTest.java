package org.poolc.api.badge;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.badge.dto.AssignBadgeRequest;
import org.poolc.api.badge.dto.PostBadgeRequest;
import org.poolc.api.badge.dto.UpdateBadgeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("badgeTest")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class BadgeAcceptanceTest extends AcceptanceTest {

    @Order(1)
    @Test
    public void 모든뱃지조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getInt("data.size()")).isEqualTo(2);
    }

    @Order(2)
    @Test
    public void 권한없는조회(){
        ExtractableResponse<Response> response = getAllBadge("incorrect_token");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Order(3)
    @Test
    public void 내뱃지조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getMyBadge(accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getInt("data.size()")).isEqualTo(1);
        assertThat(response.body().jsonPath().getString("data[0].name")).isEqualTo("test");
    }

    @Order(4)
    @Test
    public void 내뱃지조회2(){
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getMyBadge(accessToken);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getInt("data.size()")).isEqualTo(0);
    }

    @Order(5)
    @Test
    public void 뱃지등록(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        PostBadgeRequest request = PostBadgeRequest.builder()
                .description("test3")
                .imageUrl("image3.png")
                .name("test3")
                .build();
        ExtractableResponse<Response> response = postBadge(accessToken,request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response1 = getAllBadge(accessToken);
        assertThat(response1.body().jsonPath().getInt("data.size()")).isEqualTo(3);
    }

    @Order(6)
    @Test
    public void 뱃지삭제(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[2].id");

        ExtractableResponse<Response> response1 = deleteBadge(accessToken,badgeId);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getAllBadge(accessToken);
        Long badgeId2 = response2.body().jsonPath().getLong("data[1].id");
        assertThat(badgeId).isNotEqualTo(badgeId2);
    }

    @Order(7)
    @Test
    public void 겹치는_뱃지등록(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        PostBadgeRequest request = PostBadgeRequest.builder()
                .description("test3")
                .imageUrl("image3.png")
                .name("test2")
                .build();
        ExtractableResponse<Response> response = postBadge(accessToken,request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());

        ExtractableResponse<Response> response1 = getAllBadge(accessToken);
        assertThat(response1.body().jsonPath().getInt("data.size()")).isEqualTo(2);
    }

    @Order(8)
    @Test
    public void 멤버의_뱃지등록(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        PostBadgeRequest request = PostBadgeRequest.builder()
                .description("test3")
                .imageUrl("image3.png")
                .name("test3")
                .build();
        ExtractableResponse<Response> response = postBadge(accessToken,request);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

        ExtractableResponse<Response> response1 = getAllBadge(accessToken);
        assertThat(response1.body().jsonPath().getInt("data.size()")).isEqualTo(2);
    }

    @Order(9)
    @Test
    public void 뱃지_선택(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getMyBadge(accessToken);
        long a = response.body().jsonPath().getLong("data[0].id");

        ExtractableResponse<Response> response1 = selectBadge(accessToken,a);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = member(accessToken);
        assertThat(response2.body().jsonPath().getLong("badge.id")).isEqualTo(a);
    }

    @Order(10)
    @Test
    public void 내것이_아닌_뱃지_선택(){
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response1 = selectBadge(accessToken,2L);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Order(11)
    @Test
    public void 뱃지_할당_장착(){
        //뱃지 검색
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[0].id");

        //뱃지 없는것 확인
        String accessToken2 = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response1 = getMyBadge(accessToken2);
        assertThat(response1.body().jsonPath().getList("badge")).isEqualTo(null);

        //뱃지 할당
        ExtractableResponse<Response> response2 = assignBadge(accessToken, AssignBadgeRequest.builder()
                .badgeId(badgeId)
                .loginId("MEMBER_ID2")
                .build());
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

        //뱃지 할당 확인
        ExtractableResponse<Response> response3 = getMyBadge(accessToken2);
        assertThat(response3.body().jsonPath().getLong("data[0].id")).isEqualTo(badgeId);

        //뱃지 선택
        ExtractableResponse<Response> response4 = selectBadge(accessToken2,badgeId);
        assertThat(response4.statusCode()).isEqualTo(HttpStatus.OK.value());

        //뱃지 선택 확인
        ExtractableResponse<Response> response5 = member(accessToken2);
        assertThat(response5.body().jsonPath().getLong("badge.id")).isEqualTo(badgeId);
    }

    @Order(12)
    @Test
    public void 일반_멤버_뱃지_할당(){
        //뱃지 검색
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[0].id");

        //뱃지 없는것 확인
        String accessToken2 = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response1 = getMyBadge(accessToken2);
        assertThat(response1.body().jsonPath().getList("badge")).isEqualTo(null);

        //뱃지 할당
        ExtractableResponse<Response> response2 = assignBadge(accessToken, AssignBadgeRequest.builder()
                .badgeId(badgeId)
                .loginId("MEMBER_ID2")
                .build());
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());

        //뱃지 할당 안된것 확인
        ExtractableResponse<Response> response3 = getMyBadge(accessToken2);
        assertThat(response3.body().jsonPath().getList("badge")).isEqualTo(null);
    }



    @Order(13)
    @Test
    public void 수정테스트(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[0].id");

        ExtractableResponse<Response> response1 = updateBadge(accessToken,badgeId, UpdateBadgeRequest.builder()
                .description("testtest")
                .imageUrl("testimage.png")
                .name("test3")
                .build());
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response2 = getAllBadge(accessToken);
        assertThat(response2.body().jsonPath().getString("data[1].description")).isEqualTo("testtest");
    }

    @Order(14)
    @Test
    public void 수정테스트_cascade(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[1].id");

        //뱃지 수정
        ExtractableResponse<Response> response1 = updateBadge(accessToken,badgeId, UpdateBadgeRequest.builder()
                .description("testtest")
                .imageUrl("testimage.png")
                .name("아몰랑")
                .build());
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        //수정 확인
        ExtractableResponse<Response> response2 = getAllBadge(accessToken);
        assertThat(response2.body().jsonPath().getString("data[1].description")).isEqualTo("testtest");

        //뱃지 선택
        String accessToken2 = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response3 = selectBadge(accessToken2,badgeId);
        assertThat(response3.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> response4 = member(accessToken2);
        assertThat(response4.body().jsonPath().getString("badge.description")).isEqualTo("testtest");

        ExtractableResponse<Response> response5 = getMyBadge(accessToken2);
        assertThat(response5.body().jsonPath().getString("data[0].description")).isEqualTo("testtest");
    }



    @Order(15)
    @Test
    public void cascade삭제테스트(){
        //지울 뱃지 id 가져오기
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllBadge(accessToken);
        Long badgeId = response.body().jsonPath().getLong("data[1].id");

        //뱃지 선택
        String accessToken2 = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response3 = selectBadge(accessToken2,badgeId);
        assertThat(response3.statusCode()).isEqualTo(HttpStatus.OK.value());

        //삭제
        ExtractableResponse<Response> response1 = deleteBadge(accessToken,badgeId);
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());

        //삭제 확인
        ExtractableResponse<Response> response2 = getAllBadge(accessToken);
        Long badgeId2 = response2.body().jsonPath().getLong("data[0].id");
        assertThat(badgeId).isNotEqualTo(badgeId2);
        assertThat(response2.body().jsonPath().getInt("data.size()")).isEqualTo(1);

        //할당받은 뱃지까지 사라졌는지 확인
        ExtractableResponse<Response> response4 = getMyBadge(accessToken2);
        assertThat(response4.body().jsonPath().getInt("data.size()")).isEqualTo(0);

        //사용하는 뱃지 사라졌는지 확인
        ExtractableResponse<Response> response5 = member(accessToken2);
        assertThat(response5.body().jsonPath().getString("badge")).isEqualTo(null);
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

    public static ExtractableResponse<Response> postBadge(String accessToken, PostBadgeRequest request){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/badge")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> selectBadge(String accessToken, Long badgeId){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/badge/select/{badgeId}",badgeId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> member(String accessToken){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> assignBadge(String accessToken, AssignBadgeRequest request){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/badge/assign")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteBadge(String accessToken, Long badgeId){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/badge/{badgeId}",badgeId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateBadge(String accessToken, Long badgeId, UpdateBadgeRequest request){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/badge/{badgeId}",badgeId)
                .then().log().all()
                .extract();
    }
}
