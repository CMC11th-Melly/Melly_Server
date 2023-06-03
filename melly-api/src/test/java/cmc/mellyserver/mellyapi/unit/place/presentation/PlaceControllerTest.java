package cmc.mellyserver.mellyapi.unit.place.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import cmc.mellyserver.mellyapi.common.annotation.WithUser;
import cmc.mellyserver.mellyapi.common.factory.PlaceFactory;
import cmc.mellyserver.mellyapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellyapi.unit.ControllerTest;
import cmc.mellyserver.mellycore.common.enums.GroupType;

public class PlaceControllerTest extends ControllerTest {

	@DisplayName("지도의 현재 화면에서 내가 메모리를 하나라도 작성한 장소의 마커를 보여준다.")
	@WithUser
	@Test
	void show_marker_on_map_where_user_leave_memory_at_least_one() throws Exception {

		// given
		MarkedPlaceResponseDto markedPlaceResponseDto = PlaceFactory.mockMarkedPlaceResponseDto();
		given(placeService.displayMarkedPlace(anyLong(), any(GroupType.class))).willReturn(
			List.of(markedPlaceResponseDto));

		// when
		ResultActions perform = mockMvc.perform(get("/api/place/list")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.param("groupType", "FRIEND")
			.accept(MediaType.ALL));

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andDo(document("get-place-where-user-memory-exist", responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
				fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
				fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
				fieldWithPath("data.[].position").type(JsonFieldType.OBJECT).description("좌표"),
				fieldWithPath("data.[].position.lat").type(JsonFieldType.NUMBER).description("lat 좌표"),
				fieldWithPath("data.[].position.lng").type(JsonFieldType.NUMBER).description("lng 좌표"),
				fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 종류"),
				fieldWithPath("data.[].placeId").type(JsonFieldType.NUMBER).description("장소 ID"),
				fieldWithPath("data.[].memoryCount").type(JsonFieldType.NUMBER).description("장소에 속해있는 메모리 수")
			)));

		verify(placeService, times(1))
			.displayMarkedPlace(anyLong(), any(GroupType.class));
	}

	@DisplayName("장소 ID로 장소를 조회할 수 있다.")
	@WithUser
	@Test
	void get_place_by_place_id() throws Exception {

		// given
		PlaceResponseDto placeResponseDto = PlaceFactory.mockPlaceResponseDto();
		given(placeService.findPlaceByPlaceId(anyLong(), anyLong())).willReturn(placeResponseDto);

		// when
		ResultActions perform = mockMvc.perform(get("/api/place/1/search")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.ALL));

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andDo(document("get-place-where-user-memory-exist", responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
				fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
				fieldWithPath("data.placeId").type(JsonFieldType.NUMBER).description("장소 ID"),
				fieldWithPath("data.position").type(JsonFieldType.OBJECT).description("장소 좌표"),
				fieldWithPath("data.position.lat").type(JsonFieldType.NUMBER).description("lat 좌표"),
				fieldWithPath("data.position.lng").type(JsonFieldType.NUMBER).description("lng 좌표"),
				fieldWithPath("data.myMemoryCount").type(JsonFieldType.NUMBER).description("내가 작성한 메모리 수"),
				fieldWithPath("data.otherMemoryCount").type(JsonFieldType.NUMBER).description("다른 사람이 작성한 메모리 수"),
				fieldWithPath("data.isScraped").type(JsonFieldType.BOOLEAN).description("스크랩 여부"),
				fieldWithPath("data.placeCategory").type(JsonFieldType.STRING).description("장소 카테고리"),
				fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소 이름"),
				fieldWithPath("data.recommendType").type(JsonFieldType.STRING).description("추천 타입"),
				fieldWithPath("data.placeImage").type(JsonFieldType.STRING).description("장소 이미지")
			)));

		verify(placeService, times(1))
			.findPlaceByPlaceId(anyLong(), anyLong());
	}

	@DisplayName("장소에 대한 상세 정보를 얻을 수 있다.")
	@WithUser
	@Test
	void get_detail_place_info() throws Exception {

		// given
		PlaceResponseDto placeResponseDto = PlaceFactory.mockPlaceResponseDto();
		given(placeService.findPlaceByPosition(anyLong(), anyDouble(), anyDouble())).willReturn(placeResponseDto);

		// when
		ResultActions perform = mockMvc.perform(get("/api/place")
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.param("lat", "1.234")
			.param("lng", "1.234")
			.accept(MediaType.ALL));

		// then
		perform.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(200))
			.andExpect(jsonPath("$.message").value("성공"))
			.andDo(document("get-place-by-position", responseFields(
				fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
				fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
				fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
				fieldWithPath("data.placeId").type(JsonFieldType.NUMBER).description("장소 ID"),
				fieldWithPath("data.position").type(JsonFieldType.OBJECT).description("장소 좌표"),
				fieldWithPath("data.position.lat").type(JsonFieldType.NUMBER).description("lat 좌표"),
				fieldWithPath("data.position.lng").type(JsonFieldType.NUMBER).description("lng 좌표"),
				fieldWithPath("data.myMemoryCount").type(JsonFieldType.NUMBER).description("내가 작성한 메모리 수"),
				fieldWithPath("data.otherMemoryCount").type(JsonFieldType.NUMBER).description("다른 사람이 작성한 메모리 수"),
				fieldWithPath("data.isScraped").type(JsonFieldType.BOOLEAN).description("스크랩 여부"),
				fieldWithPath("data.placeCategory").type(JsonFieldType.STRING).description("장소 카테고리"),
				fieldWithPath("data.placeName").type(JsonFieldType.STRING).description("장소 이름"),
				fieldWithPath("data.recommendType").type(JsonFieldType.STRING).description("추천 타입"),
				fieldWithPath("data.placeImage").type(JsonFieldType.STRING).description("장소 이미지")
			)));

		verify(placeService, times(1))
			.findPlaceByPosition(anyLong(), anyDouble(), anyDouble());
	}
}
