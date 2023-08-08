package cmc.mellyserver.mellyapi.unit.user.presentation;

import cmc.mellyserver.mellyapi.common.annotation.WithUser;
import cmc.mellyserver.mellyapi.unit.ControllerTest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentRequest;
import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentResponse;
import static cmc.mellyserver.mellycore.common.enums.AgeGroup.THREE;
import static cmc.mellyserver.mellycore.common.enums.Gender.MALE;
import static cmc.mellyserver.mellycore.common.fixture.UserFixtures.모카_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {

    @DisplayName("자신의 프로필 정보를 조회한다.")
    @Test
    @WithUser
    void 자신의_프로필_정보를_조회한다() throws Exception {

        // given
        given(userProfileService.getUserProfile(any())).willReturn(모카_응답);
        given(userProfileService.checkImageStorageVolumeLoginUserUse(any())).willReturn(100);

        // when & then
        mockMvc.perform(get("/api/users/my-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("user/my-profile",
                        getDocumentRequest(),
                        getDocumentResponse()
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("자신의 프로필을 수정한다.")
    @Test
    @WithUser
    void 자신의_프로필_정보를_수정한다() throws Exception {

        // given
        willDoNothing()
                .given(userProfileService)
                .updateUserProfile(anyLong(), any(ProfileUpdateRequestDto.class));

        ProfileUpdateRequest 유저_프로필_수정_정보 = new ProfileUpdateRequest("머식", MALE.name(), THREE.name());


        // when & then
        mockMvc.perform(patch("/api/users/my-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(유저_프로필_수정_정보))
                )
                .andDo(print())
                .andDo(document("user/update",
                        getDocumentRequest(),
                        getDocumentResponse()
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("자신의 프로필 이미지를 수정한다.")
    @Test
    @WithUser
    void 자신의_프로필_이미지를_수정한다() throws Exception {

        // given
        willDoNothing()
                .given(userProfileService)
                .updateUserProfileImage(anyLong(), any(MultipartFile.class));

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/my-profile/profile-image")
                        .file("이미지", "이미지".getBytes())
                        .with(csrf()))
                .andDo(print())
                .andDo(document("user/update-profile-image",
                        getDocumentRequest(),
                        getDocumentResponse()
                ))
                .andExpect(status().isNoContent());
    }


    @DisplayName("마이페이지에서 로그인한 사용자가 속해있는 그룹 리스트를 조회할 수 있다.")
    @Test
    @WithUser
    void getGroupsLoginUserAssociated() throws Exception {

        // given
        List<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos = List.of(
                new GroupLoginUserParticipatedResponseDto(1L, 1, "친구",
                        List.of(new UserDto(1L, "testImage.png", "jemin", true),
                                new UserDto(2L, "testImage.png", "jemin2", false)), GroupType.FRIEND,
                        "link"),
                new GroupLoginUserParticipatedResponseDto(2L, 2, "동료",
                        List.of(new UserDto(1L, "testImage.png", "jemin", false),
                                new UserDto(2L, "testImage.png", "jemin2", true)), GroupType.COMPANY,
                        "link")
        );

        List<GroupLoginUserParticipatedResponse> groupLoginUserParticipatedResponses = UserAssembler.groupLoginUserParticipatedResponses(
                groupLoginUserParticipatedResponseDtos);

        given(userService.findGroupListLoginUserParticiated(anyLong()))
                .willReturn(groupLoginUserParticipatedResponseDtos);

        // when
        ResultActions perform = mockMvc.perform(get("/api/user/group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL));

        // then
        String body = perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andReturn().getResponse().getContentAsString();

        perform.andDo(document("get-group-list",
                getDocumentRequest(),
                getDocumentResponse()));
    }
//
//    @DisplayName("마이페이지에서 사용자가 스크랩한 장소의 스크랩 타입별 개수를 볼 수 있다.")
//    @Test
//    @WithUser
//    void getScrapedPlaceCount() throws Exception {
//
//        // given
//        List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos = List.of(
//                new PlaceScrapCountResponseDto(ScrapType.FRIEND, 2L),
//                new PlaceScrapCountResponseDto(ScrapType.COMPANY, 3L)
//        );
//
//        List<PlaceScrapCountResponse> placeScrapCountResponses = UserAssembler.placeScrapCountResponses(
//                placeScrapCountResponseDtos);
//
//        given(placeScrapService.countByPlaceScrapType(anyLong()))
//                .willReturn(placeScrapCountResponseDtos);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/user/place/scrap/count")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.ALL));
//
//        // then
//        String body = perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andReturn().getResponse().getContentAsString();
//
//        verify(placeScrapService, times(1))
//                .countByPlaceScrapType(anyLong());
//
//        assertThat(dataExtractor.extractDataField(body)).isEqualTo(
//                objectMapper.writeValueAsString(placeScrapCountResponses));
//
//        perform.andDo(document("get-scrap-count",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
//                        fieldWithPath("data.[].scrapType").type(JsonFieldType.STRING)
//                                .description("스크랩 종류"),
//                        fieldWithPath("data.[].scrapCount").type(JsonFieldType.NUMBER)
//                                .description("스크랩 종류별 장소 개수")
//                )));
//    }
//
//    @DisplayName("마이페이지에서 사용자가 스크랩한 장소들을 조회할 수 있다.")
//    @Test
//    @WithUser
//    void getPlacesUserScraped() throws Exception {
//
//        // given
//        List<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos = List.of(
//                new ScrapedPlaceResponseDto(1L, new Position(1.1234, 1.1234), 10L, 20L, true, "카페",
//                        "투썸플레이스",
//                        GroupType.FRIEND, "testImage.png"),
//                new ScrapedPlaceResponseDto(2L, new Position(1.1234, 1.1234), 10L, 20L, true, "카페",
//                        "스타벅스",
//                        GroupType.FRIEND, "testImage.png")
//        );
//
//        Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtoSlice = new SliceImpl<>(
//                scrapedPlaceResponseDtos,
//                PageRequest.of(0, 2), false);
//        Slice<ScrapedPlaceResponse> scrapedPlaceResponses = UserAssembler.scrapedPlaceResponses(
//                scrapedPlaceResponseDtoSlice);
//
//        given(placeScrapService.findScrapedPlace(any(Pageable.class), anyLong(),
//                any(ScrapType.class)))
//                .willReturn(scrapedPlaceResponseDtoSlice);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/user/place/scrap")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .param("pageable", String.valueOf(PageRequest.of(0, 2)))
//                .param("scrapType", "FRIEND")
//                .accept(MediaType.ALL));
//
//        // then
//        String body = perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andReturn().getResponse().getContentAsString();
//
//        verify(placeScrapService, times(1))
//                .findScrapedPlace(any(Pageable.class), anyLong(), any(ScrapType.class));
//
//        assertThat(dataExtractor.extractDataField(body)).isEqualTo(
//                objectMapper.writeValueAsString(scrapedPlaceResponses));
//
//        perform.andDo(document("get-scraped-place",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
//                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("컨텐츠"),
//                        fieldWithPath("data.content.[].placeId").type(JsonFieldType.NUMBER)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].position").type(JsonFieldType.OBJECT)
//                                .description("좌표"),
//                        fieldWithPath("data.content.[].position.lat").type(JsonFieldType.NUMBER)
//                                .description("lat 좌표"),
//                        fieldWithPath("data.content.[].position.lng").type(JsonFieldType.NUMBER)
//                                .description("lng 좌표"),
//                        fieldWithPath("data.content.[].myMemoryCount").type(JsonFieldType.NUMBER)
//                                .description("내가 작성한 메모리 수"),
//                        fieldWithPath("data.content.[].otherMemoryCount").type(JsonFieldType.NUMBER)
//                                .description("다른 사람이 작성한 메모리 수"),
//                        fieldWithPath("data.content.[].isScraped").type(JsonFieldType.BOOLEAN)
//                                .description("스크랩 여부"),
//                        fieldWithPath("data.content.[].placeCategory").type(JsonFieldType.STRING)
//                                .description("장소 카테고리"),
//                        fieldWithPath("data.content.[].placeName").type(JsonFieldType.STRING)
//                                .description("장소 이름"),
//                        fieldWithPath("data.content.[].recommendType").type(JsonFieldType.STRING)
//                                .description("추천 종류"),
//                        fieldWithPath("data.content.[].placeImage").type(JsonFieldType.STRING)
//                                .description("장소 이미지 URL"),
//                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("사이즈"),
//                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
//                                .description("한페이지 컨텐츠 수"),
//                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬"),
//                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 기준 존재 유무"),
//                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 안됨"),
//                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 됨"),
//                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
//                                .description("처음 페이지 여부"),
//                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
//                                .description("마지막 페이지 여부"),
//                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
//                                .description("전체 컨텐츠 수"),
//                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
//                                .description("데이터 존재 유무"),
//                        subsectionWithPath("data.pageable").ignored()
//                )));
//    }
//
//    @DisplayName("마이페이지에서 사용자가 저장한 이미지 용량을 볼 수 있다.")
//    @Test
//    @WithUser
//    void getUserImageUsageVolume() throws Exception {
//
//        // given
//        given(userService.checkImageStorageVolumeLoginUserUse(anyString()))
//                .willReturn(1000);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/user/volume")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.ALL));
//
//        // then
//        String body = perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andReturn().getResponse().getContentAsString();
//
//        verify(userService, times(1))
//                .checkImageStorageVolumeLoginUserUse(anyString());
//
//        assertThat(dataExtractor.extractDataField(body)).isEqualTo(
//                objectMapper.writeValueAsString(1000));
//
//        perform.andDo(document("get-user-image-usage",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("이미지 저장 용량")
//                )));
//    }
//
//    @DisplayName("새로운 그룹에 참여할 수 있다.")
//    @Test
//    @WithUser
//    void participateToGroup() throws Exception {
//
//        // given
//        ParticipateGroupRequest participateGroupRequest = new ParticipateGroupRequest(1L);
//        Mockito.doNothing().when(groupService).participateToGroup(anyLong(), anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/user/group")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(participateGroupRequest))
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(groupService, times(1))
//                .participateToGroup(anyLong(), anyLong());
//
//        perform.andDo(document("participate-group",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 응답 포맷")
//                )));
//    }
//
//    @DisplayName("유저 프로필을 업데이트 할 수 있다.")
//    @Test
//    @WithUser
//    void updateProfile() throws Exception {
//
//        // given
//
//        // when
//        ResultActions perform = mockMvc.perform(multipart(HttpMethod.PUT, "/api/user/profile")
//                .file("images", "testImage1".getBytes())
//                .param("nickname", "jemin")
//                .param("gender", "MALE")
//                .param("ageGroup", "TWO")
//                .param("deleteImage", "true")
//                .with(csrf()));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(userService, times(1))
//                .updateLoginUserProfile(any(ProfileUpdateRequestDto.class));
//
//        perform.andDo(document("update-profile",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 응답 포맷")
//                )));
//    }
//
//    @DisplayName("마이페이지에서 유저가 속한 그룹이 작성한 메모리 목록을 조회할 수 있다.")
//    @Test
//    @WithUser
//    void getMemoryBelongToMyGroup() throws Exception {
//
//        // given
//        List<MemoryResponseDto> responseDtos = List.of(
//                new MemoryResponseDto(1L, "테스트 장소", 2L, "다음에 또 가고싶다!", "친구랑 오기 좋아요",
//                        GroupType.FRIEND, "동기들", 5L, true,
//                        LocalDateTime.of(2023, 5, 30, 20, 30)),
//                new MemoryResponseDto(2L, "두번째 장소", 3L, "내일 또 가요", "동료랑 오기 좋아요", GroupType.COMPANY,
//                        "동료들", 4L, true,
//                        LocalDateTime.of(2023, 5, 29, 20, 30))
//        );
//
//        Slice<MemoryResponseDto> memoryResponseDtos = new SliceImpl<>(responseDtos,
//                PageRequest.of(0, 2), false);
//        Slice<MemoryResponse> memoryResponses = MemoryAssembler.memoryResponses(
//                memoryResponseDtos);
//
//        given(userService.findMemoriesUsersBelongToMyGroupWrite(any(Pageable.class), anyLong(),
//                anyLong()))
//                .willReturn(memoryResponseDtos);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/user/group/1/memory")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .param("userId", "1")
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        String body = perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andReturn().getResponse().getContentAsString();
//
//        verify(userService, times(1))
//                .findMemoriesUsersBelongToMyGroupWrite(any(Pageable.class), anyLong(), anyLong());
//
//        assertThat(dataExtractor.extractDataField(body)).isEqualTo(
//                objectMapper.writeValueAsString(memoryResponses));
//
//        perform.andDo(document("get-memories-belong-my-group",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 응답 포맷"),
//                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("컨텐츠"),
//                        fieldWithPath("data.content.[].placeId").type(JsonFieldType.NUMBER)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].placeName").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].memoryId").type(JsonFieldType.NUMBER)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].memoryImages").type(JsonFieldType.NULL)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].title").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].content").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].groupType").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].groupName").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].stars").type(JsonFieldType.NUMBER)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].keyword").type(JsonFieldType.NULL)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].loginUserWrite").type(JsonFieldType.BOOLEAN)
//                                .description("장소 ID"),
//                        fieldWithPath("data.content.[].visitedDate").type(JsonFieldType.STRING)
//                                .description("장소 ID"),
//                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("사이즈"),
//                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
//                                .description("한페이지 컨텐츠 수"),
//                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬"),
//                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 기준 존재 유무"),
//                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 안됨"),
//                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
//                                .description("정렬 됨"),
//                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
//                                .description("처음 페이지 여부"),
//                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
//                                .description("마지막 페이지 여부"),
//                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
//                                .description("전체 컨텐츠 수"),
//                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
//                                .description("데이터 존재 유무"),
//                        subsectionWithPath("data.pageable").ignored()
//
//                )));
//    }

}
