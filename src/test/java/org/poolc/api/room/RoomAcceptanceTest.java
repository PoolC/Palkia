package org.poolc.api.room;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.room.domain.RoomReservationRequest;
import org.poolc.api.room.dto.RoomPostRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("roomTest")
public class RoomAcceptanceTest extends AcceptanceTest {

    @Test
    public void 동방예약조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getString("data[0].purpose")).isEqualTo("테스트");
    }

    @Test
    public void 권한_없는_조회(){
        ExtractableResponse<Response> response = getAllRoomRequest("incorrect_token","2023-01-01","2023-01-02");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void 너무_많은_조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllRoomRequest(accessToken, "2023-01-01", "2025-01-01");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void 잘못된_조회(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> response = getAllRoomRequest(accessToken, "2023-02-01", "2023-01-01");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void 동방예약_생성(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        RoomReservationRequest roomReservationRequest = new RoomReservationRequest(LocalDate.of(2023,1,3), LocalTime.of(10,00), LocalTime.of(12,00),"test");
        ExtractableResponse<Response> response = postRoomReservation(accessToken, roomReservationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 겹치는시간_예약(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        RoomReservationRequest roomReservationRequest = new RoomReservationRequest(LocalDate.of(2023,1,1), LocalTime.of(10,00), LocalTime.of(12,00),"test");
        ExtractableResponse<Response> response = postRoomReservation(accessToken, roomReservationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void 권한_없는_예약(){
        RoomReservationRequest roomReservationRequest = new RoomReservationRequest(LocalDate.of(2023,1,1), LocalTime.of(10,00), LocalTime.of(12,00),"test");
        ExtractableResponse<Response> response = postRoomReservation("incorrect_token", roomReservationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void 시간_단위가_이상함(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        RoomReservationRequest roomReservationRequest = new RoomReservationRequest(LocalDate.of(2023,1,1), LocalTime.of(10,01), LocalTime.of(12,00),"test");
        ExtractableResponse<Response> response = postRoomReservation(accessToken, roomReservationRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

//    @Test
//    public void 예약이_과거임(){
//        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
//                .as(AuthResponse.class)
//                .getAccessToken();
//        RoomReservationRequest roomReservationRequest = new RoomReservationRequest(LocalDate.of(2023,1,1), LocalTime.of(10,00), LocalTime.of(12,00),"test");
//        ExtractableResponse<Response> response = postRoomReservation(accessToken, roomReservationRequest);
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//    }

    @Test
    public void 예약정보_수정(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        RoomPostRequest roomPostRequest = new RoomPostRequest(LocalDate.of(2023,01,02),LocalTime.of(14,0),LocalTime.of(16,0),"test");
        ExtractableResponse<Response> response = putRoomReservation(accessToken, roomPostRequest,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 다른사람의_예약정보_수정(){
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        RoomPostRequest roomPostRequest = new RoomPostRequest(LocalDate.of(2023,01,02),LocalTime.of(14,0),LocalTime.of(16,0),"test");
        ExtractableResponse<Response> response = putRoomReservation(accessToken, roomPostRequest,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void 수정_이상한_시간(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        RoomPostRequest roomPostRequest = new RoomPostRequest(LocalDate.of(2023,01,02),LocalTime.of(14,1),LocalTime.of(16,0),"test");
        ExtractableResponse<Response> response = putRoomReservation(accessToken, roomPostRequest,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void 수정_겹치는_시간(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        RoomPostRequest roomPostRequest = new RoomPostRequest(LocalDate.of(2023,01,02),LocalTime.of(14,0),LocalTime.of(16,0),"test");
        ExtractableResponse<Response> response = putRoomReservation(accessToken, roomPostRequest,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    public void 예약정보_삭제(){
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        ExtractableResponse<Response> response = deleteRoomReservation(accessToken,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void 권한없는_삭제(){
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        ExtractableResponse<Response> response = deleteRoomReservation(accessToken,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void 임원진이_삭제(){
        String accessToken = loginRequest("MEMBER_ID3", "MEMBER_PASSWORD3")
                .as(AuthResponse.class)
                .getAccessToken();
        ExtractableResponse<Response> temp = getAllRoomRequest(accessToken,"2023-01-01","2023-01-02");
        long id = temp.body().jsonPath().getLong("data[0].id");
        ExtractableResponse<Response> response = deleteRoomReservation(accessToken,id);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> getAllRoomRequest(String accessToken, String startDate, String endDate){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("start",startDate)
                .param("end",endDate)
                .when().get("/room")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRoomReservation(String accessToken, RoomReservationRequest request){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/room")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRoomReservation(String accessToken, RoomPostRequest request, Long reservationId){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/room/{reservationId}", reservationId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRoomReservation(String accessToken, Long reservationId){
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/room/{reservationId}", reservationId)
                .then().log().all()
                .extract();
    }
}
