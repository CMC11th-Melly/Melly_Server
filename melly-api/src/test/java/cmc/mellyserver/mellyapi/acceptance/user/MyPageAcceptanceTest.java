package cmc.mellyserver.mellyapi.acceptance.user;

import static cmc.mellyserver.mellyapi.acceptance.fixtures.AuthAcceptanceFixtures.로그인_진행후_액세스_토큰을_반환한다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.MemoryAcceptanceFixtures.새로운_메모리를_등록한다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.UserAcceptanceFixtures.로그인한_사용자가_마이페이지에서_속해있는_그룹을_조회한다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.UserAcceptanceFixtures.로그인한_사용자가_마이페이지에서_자신의_프로필_수정_화면을_띄운다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.UserAcceptanceFixtures.로그인한_사용자가_마이페이지에서_자신이_작성한_메모리를_조회한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cmc.mellyserver.mellyapi.acceptance.AcceptanceTest;
import cmc.mellyserver.mellyapi.common.response.CommonResponse;
import cmc.mellyserver.mellyapi.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("회원 마이페이지 관련 기능")
public class MyPageAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인한 사용자가 마이페이지에서 자신이 속한 그룹을 조회하면 상태 코드 200과 그룹 리스트를 얻을 수 있다.")
    @Test
    void 로그인한_사용자는_마이페이지에서_자신이_속한_그룹을_조회할수있다() {

        // given
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();

        // when
        ExtractableResponse<Response> response = 로그인한_사용자가_마이페이지에서_속해있는_그룹을_조회한다(accessToken);
        CommonResponse<List<GroupLoginUserParticipatedResponse>> result = response.as(
                new TypeRef<CommonResponse<List<GroupLoginUserParticipatedResponse>>>() {
                });

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(result.getData()).hasSize(0);
        });
    }

    @DisplayName("로그인한 사용자가 마이페이지에서 프로필 수정을 누르면 상태 코드 200와 기존 유저 프로필 정보를 받을 수 있다.")
    @Test
    void 로그인한_사용자는_마이페이지에서_자신의_프로필을_수정할수있다() {

        // given
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();

        // when
        ExtractableResponse<Response> response = 로그인한_사용자가_마이페이지에서_자신의_프로필_수정_화면을_띄운다(accessToken);
        CommonResponse<ProfileUpdateFormResponseDto> result = response.as(
                new TypeRef<CommonResponse<ProfileUpdateFormResponseDto>>() {
                });

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(result.getData().getNickname()).isEqualTo("제민");
        });
    }

    @DisplayName("로그인한 사용자가 마이페이지에서 본인이 작성한 메모리를 조회하면 상태 코드 200을 받을 수 있다.")
    @Test
    void 로그인한_사용자는_마이페이지에서_자신이_작성한_메모리를_조회한다() throws IOException {

        // given
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();
        새로운_메모리를_등록한다(accessToken);

        // when
        ExtractableResponse<Response> response = 로그인한_사용자가_마이페이지에서_자신이_작성한_메모리를_조회한다(accessToken);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
        });
    }
}
