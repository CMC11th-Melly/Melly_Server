package cmc.mellyserver.mellyapi.acceptance.fixtures;

import cmc.mellyserver.mellycommon.enums.GroupType;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;

public class UserAcceptanceFixtures {

    public static ExtractableResponse<Response> 로그인한_사용자가_마이페이지에서_속해있는_그룹을_조회한다(
            final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/user/group")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인한_사용자가_마이페이지에서_자신의_프로필_수정_화면을_띄운다(
            final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/user/profile")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인한_사용자가_마이페이지에서_자신이_작성한_메모리를_조회한다(
            final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("pageable", PageRequest.of(0, 10))
                .param("groupType", GroupType.ALL)
                .when()
                .get("/api/user/memory")
                .then().log().all()
                .extract();
    }
}