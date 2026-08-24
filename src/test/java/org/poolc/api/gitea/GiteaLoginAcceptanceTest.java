package org.poolc.api.gitea;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.AuthAcceptanceTest;
import org.poolc.api.gitea.dto.GiteaLoginTicketResponse;
import org.poolc.api.poolc.PoolcAcceptanceTest;
import org.poolc.api.poolc.dto.UpdatePoolcRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("memberTest")
public class GiteaLoginAcceptanceTest extends AcceptanceTest {
    private static final String GITEA_PROXY_API_KEY = "test-gitea-proxy-key";

    @Test
    void memberCanCreateAndConsumeGiteaLoginTicket() {
        String accessToken = AuthAcceptanceTest.memberLogin();

        ExtractableResponse<Response> createResponse = createLoginTicket(accessToken);
        GiteaLoginTicketResponse ticket = createResponse.as(GiteaLoginTicketResponse.class);

        ExtractableResponse<Response> validateResponse = validateLoginTicket(ticket.getTicket(), GITEA_PROXY_API_KEY);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ticket.getTicket()).isNotBlank();
        assertThat(ticket.getLoginUrl()).contains(ticket.getTicket());
        assertThat(validateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(validateResponse.header("X-WEBAUTH-USER")).isEqualTo(AuthAcceptanceTest.member_id);
        assertThat(validateResponse.header("X-WEBAUTH-EMAIL")).isEqualTo("example@email.com");
        assertThat(validateResponse.header("X-WEBAUTH-FULLNAME")).isEqualTo("MEMBER_NAME");
        assertThat(validateResponse.jsonPath().getString("loginID")).isEqualTo(AuthAcceptanceTest.member_id);
        assertThat(validateResponse.jsonPath().getString("email")).isEqualTo("example@email.com");
        assertThat(validateResponse.jsonPath().getString("name")).isEqualTo("MEMBER_NAME");
        assertThat(validateResponse.jsonPath().getBoolean("admin")).isFalse();
    }

    @Test
    void giteaLoginTicketCanBeUsedOnlyOnce() {
        String accessToken = AuthAcceptanceTest.memberLogin();
        String ticket = createLoginTicket(accessToken).as(GiteaLoginTicketResponse.class).getTicket();

        ExtractableResponse<Response> firstResponse = validateLoginTicket(ticket, GITEA_PROXY_API_KEY);
        ExtractableResponse<Response> secondResponse = validateLoginTicket(ticket, GITEA_PROXY_API_KEY);

        assertThat(firstResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(secondResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void unacceptedMemberCannotCreateGiteaLoginTicket() {
        String adminAccessToken = AuthAcceptanceTest.adminLogin();
        UpdatePoolcRequest openSubscription = new UpdatePoolcRequest(
                "전영주",
                "01067679584",
                "공A 537호",
                null,
                "프로그래밍 동아리",
                null,
                true,
                null
        );
        PoolcAcceptanceTest.updatePoolcInfo(adminAccessToken, openSubscription);
        String accessToken = AuthAcceptanceTest.unacceptanceLogin();

        ExtractableResponse<Response> response = createLoginTicket(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void invalidProxyApiKeyCannotValidateGiteaLoginTicket() {
        String accessToken = AuthAcceptanceTest.memberLogin();
        String ticket = createLoginTicket(accessToken).as(GiteaLoginTicketResponse.class).getTicket();

        ExtractableResponse<Response> response = validateLoginTicket(ticket, "wrong-api-key");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private static ExtractableResponse<Response> createLoginTicket(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/gitea/login-ticket")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> validateLoginTicket(String ticket, String apiKey) {
        return RestAssured
                .given().log().all()
                .header("X-API-KEY", apiKey)
                .header("X-PoolC-Gitea-Ticket", ticket)
                .when().get("/gitea/login-ticket/validate")
                .then().log().all()
                .extract();
    }
}
