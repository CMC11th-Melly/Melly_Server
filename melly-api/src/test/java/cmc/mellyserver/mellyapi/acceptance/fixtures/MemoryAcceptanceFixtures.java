package cmc.mellyserver.mellyapi.acceptance.fixtures;

import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;

//@RequestPart(name = "images", required = false) List<MultipartFile> images,
//@Valid @RequestPart(name = "memoryData") MemoryCreateRequest memoryCreateRequest
public class MemoryAcceptanceFixtures {

    public static ExtractableResponse<Response> 새로운_메모리를_등록한다(final String accessToken)
            throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage", "test",
                "multipart/form-data", new FileInputStream(
                "/Users/seojemin/IdeaProjects/Melly_Server/melly-api/src/test/java/resources/image/testimage.png"));

        MemoryCreateRequest memoryCreateRequest = MemoryCreateRequest.builder().lng(1.234)
                .lat(1.234)
                .keyword(List.of("행복해요")).title("메모리 제목").content("메모리 컨텐츠 20자 이상 적어야 합니다. 주의!")
                .openType(
                        OpenType.ALL)
                .placeName("테스트 장소").star(5L).build();

        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when()
                .multiPart("memoryData", memoryCreateRequest, "application/json")
                .post("/api/memory")
                .then().log().all()
                .extract();
    }

}
