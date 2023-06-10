package cmc.mellyserver.mellyapi.acceptance.scrap;

import static cmc.mellyserver.mellyapi.acceptance.fixtures.AuthAcceptanceFixtures.로그인_진행후_액세스_토큰을_반환한다;

import cmc.mellyserver.mellyapi.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("장소 스크랩 관련 테스트")
public class ScrapAcceptanceTest extends AcceptanceTest {


    @DisplayName("로그인한 사용자가 장소를 스크랩하면 상태 코드 200을 반환한다.")
    @Test
    void 로그인한_사용자가_장소를_스크랩하면_상태코드_200을_반환한다() {
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();


    }


    @DisplayName("로그인한 사용자가 장소 스크랩을 취소하면 200을 반환한다.")
    @Test
    void 로그인한_사용자가_장소를_스크랩_취소하면_상태코드_200을_반환한다() {
        String accessToken = 로그인_진행후_액세스_토큰을_반환한다();


    }


}
