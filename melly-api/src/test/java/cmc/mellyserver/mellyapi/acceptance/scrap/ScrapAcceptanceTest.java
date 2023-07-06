package cmc.mellyserver.mellyapi.acceptance.scrap;

import static cmc.mellyserver.mellyapi.acceptance.fixtures.AuthAcceptanceFixtures.로그인_진행후_액세스_토큰을_반환한다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.CommonAcceptanceFixtures.상태코드_200이_반환된다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.ScrapAcceptanceFixtures.사용자가_장소를_스크랩_취소한다;
import static cmc.mellyserver.mellyapi.acceptance.fixtures.ScrapAcceptanceFixtures.사용자가_장소를_스크랩한다;
import static org.junit.jupiter.api.Assertions.assertAll;

import cmc.mellyserver.mellyapi.acceptance.AcceptanceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("장소 스크랩 관련 테스트")
public class ScrapAcceptanceTest extends AcceptanceTest {


    @DisplayName("로그인한 사용자가 장소를 스크랩하면 상태 코드 200을 반환한다.")
    @Test
    void 로그인한_사용자가_장소를_스크랩하면_상태코드_200을_반환한다() throws JsonProcessingException {

        // given
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();

        // when
        ExtractableResponse<Response> response = 사용자가_장소를_스크랩한다(accessToken);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
        });

    }


    @DisplayName("로그인한 사용자가 장소 스크랩을 취소하면 200을 반환한다.")
    @Test
    void 로그인한_사용자가_장소를_스크랩_취소하면_상태코드_200을_반환한다() throws JsonProcessingException {

        // given
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();
        사용자가_장소를_스크랩한다(accessToken);

        // when
        ExtractableResponse<Response> response = 사용자가_장소를_스크랩_취소한다(accessToken);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
        });
    }


}
