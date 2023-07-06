package cmc.mellyserver.mellyapi.unit.scrap.presentation;

import cmc.mellyserver.mellyapi.common.annotation.WithUser;
import cmc.mellyserver.mellyapi.common.factory.PlaceScrapFactory;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellyapi.unit.ControllerTest;
import cmc.mellyserver.mellycore.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentRequest;
import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlaceScrapControllerTest extends ControllerTest {

    @DisplayName("원하는 장소를 스크랩할 수 있다.")
    @WithUser
    @Test
    void createScrap() throws Exception {

        // given
        ScrapRequest scrapRequest = PlaceScrapFactory.mockScrapRequest();
        Mockito.doNothing().when(placeScrapService).createScrap(any(CreatePlaceScrapRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(post("/api/place/scrap")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(scrapRequest))
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(placeScrapService, times(1))
                .createScrap(any(CreatePlaceScrapRequestDto.class));

        perform.andDo(document("create-scrap",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("lat").description("lat 좌표"),
                        fieldWithPath("lng").description("lng 좌표"),
                        fieldWithPath("scrapType").description("스크랩 종류"),
                        fieldWithPath("placeCategory").description("장소 종류"),
                        fieldWithPath("placeName").description("장소 이름")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));

    }

    @DisplayName("스크랩을 취소할 수 있다.")
    @WithUser
    @Test
    void cancelScrap() throws Exception {

        // given
        ScrapCancelRequest scrapCancelRequest = PlaceScrapFactory.mockScrapCancelRequest();
        Mockito.doNothing().when(placeScrapService).removeScrap(anyLong(), anyDouble(), anyDouble());

        // when
        ResultActions perform = mockMvc.perform(delete("/api/place/scrap")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(scrapCancelRequest))
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(placeScrapService, times(1))
                .removeScrap(anyLong(), anyDouble(), anyDouble());

        perform.andDo(document("cancel-scrap",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("lat").description("lat 좌표"),
                        fieldWithPath("lng").description("lng 좌표")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));

    }
}
