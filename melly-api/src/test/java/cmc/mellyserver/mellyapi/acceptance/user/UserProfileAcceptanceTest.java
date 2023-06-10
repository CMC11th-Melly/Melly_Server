package cmc.mellyserver.mellyapi.acceptance.user;

import static org.assertj.core.api.Assertions.assertThat;

import cmc.mellyserver.mellyapi.acceptance.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("회원 프로필 관련 기능")
class UserProfileAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인하지 않은 사용자는 프로필을 수정할 수 없다.")
    @Test
    void invalid_user_cant_edit_profile_info() {
        ExtractableResponse<Response> result = RestAssured.given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("nickname", "변경된 닉네임")
                .param("gender", "FEMALE")
                .param("ageGroup", "SEVEN")
                .param("deleteImage", "true")
                .when().put("/api/user/profile")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("로그인한 사용자는 프로필 수정할 수 있다.")
    @Test
    void user_edit_profile_info() {
        ExtractableResponse<Response> result = RestAssured.given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .param("nickname", "변경된 닉네임")
                .param("gender", "FEMALE")
                .param("ageGroup", "SEVEN")
                .param("deleteImage", "true")
                .when().put("/api/user/profile")
                .then()
                .extract();

        assertThat(result.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
