package cmc.mellyserver.mellyappexternalapi.acceptance.fixtures;

import cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.request.AuthLoginRequest;
import cmc.mellyserver.mellyappexternalapi.auth.presentation.dto.response.LoginResponse;
import cmc.mellyserver.mellyappexternalapi.common.response.CommonResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceFixtures {


    public static String 로그인_진행후_액세스_토큰을_반환한다() {

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new AuthLoginRequest("jemin3161@naver.com", "1234mm1234"))
                .when().post("/auth/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        CommonResponse<LoginResponse> common = response.as(
                new TypeRef<CommonResponse<LoginResponse>>() {
                });
        return common.getData().getToken();

    }
}
