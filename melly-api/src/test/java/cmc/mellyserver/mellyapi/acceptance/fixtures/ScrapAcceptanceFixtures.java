package cmc.mellyserver.mellyapi.acceptance.fixtures;

import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycore.common.enums.ScrapType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class ScrapAcceptanceFixtures {

    public static ExtractableResponse<Response> 사용자가_장소를_스크랩한다(final String accessToken)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ScrapRequest scrapRequest = ScrapRequest.builder().lng(1.234).lat(1.234)
                .scrapType(ScrapType.FRIEND).placeName("테스트 장소").placeCategory("카페").build();

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(scrapRequest))
                .when()
                .post("/api/place/scrap")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사용자가_장소를_스크랩_취소한다(final String accessToken)
            throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ScrapCancelRequest scrapCancelRequest = ScrapCancelRequest.builder().lng(1.234).lat(1.234)
                .build();

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(scrapCancelRequest))
                .when()
                .delete("/api/place/scrap")
                .then().log().all()
                .extract();
    }
}
