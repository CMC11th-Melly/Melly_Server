package cmc.mellyserver.mellyapi.user;

import cmc.mellyserver.mellyapi.ControllerTest;
import cmc.mellyserver.mellyapi.common.annotation.WithUser;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryListResponse;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendActivity;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendGroup;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendPlace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentRequest;
import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentResponse;
import static cmc.mellyserver.mellycore.user.domain.enums.AgeGroup.THREE;
import static cmc.mellyserver.mellycore.user.domain.enums.Gender.MALE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        given(userProfileService.getUserProfile(any()))
                .willReturn(ProfileResponseDto.of(1L, "모카", "jemin3161@naver.com", null));

        given(userProfileService.checkImageStorageVolumeLoginUserUse(any()))
                .willReturn(100);

        // when & then
        mockMvc.perform(get("/api/users/my-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("user/my-profile",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("data.profileImage").type(JsonFieldType.VARIES).description("프로필 이미지"),
                                fieldWithPath("data.imageVolume").type(JsonFieldType.NUMBER).description("이미지 저장 용량")
                        )
                ))
                .andExpect(status().isOk());
    }


    @DisplayName("자신의 프로필을 수정한다.")
    @Test
    @WithUser
    void 자신의_프로필_정보를_수정한다() throws Exception {

        // given
        willDoNothing().given(userProfileService)
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
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("수정 닉네임"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("수정 성별"),
                                fieldWithPath("ageGroup").type(JsonFieldType.STRING).description("수정 연령대")
                        )
                ))
                .andExpect(status().isOk());
    }


    @DisplayName("자신의 프로필 이미지를 수정한다.")
    @Test
    @WithUser
    void 자신의_프로필_이미지를_수정한다() throws Exception {

        // given
        willDoNothing().given(userProfileService)
                .updateUserProfileImage(anyLong(), any(MultipartFile.class));

        mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/my-profile/profile-image")
                        .file("이미지", "이미지".getBytes())
                        .with(csrf()))
                .andDo(print())
                .andDo(document("user/update-profile-image",
                        getDocumentRequest(),
                        getDocumentResponse()
                ))
                .andExpect(status().isOk());
    }


    @DisplayName("최초 회원가입 시 설문 조사 결과를 저장한다.")
    @Test
    @WithUser
    void 최초_회원가입시_설문조사_결과를_저장한다() throws Exception {
        // given
        SurveyRequest surveyRequest = SurveyRequest.builder()
                .recommendPlace(RecommendPlace.PLACE1)
                .recommendGroup(RecommendGroup.FRIEND)
                .recommendActivity(RecommendActivity.CAFE).build();

        willDoNothing().given(userSurveyService)
                .createSurvey(anyLong(), any(SurveyRequestDto.class));

        // when
        mockMvc.perform(post("/api/users/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(surveyRequest))
                )
                .andDo(print())
                .andDo(document("user/create-survey",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("recommendPlace").type(JsonFieldType.STRING).description("추천 장소"),
                                fieldWithPath("recommendGroup").type(JsonFieldType.STRING).description("추천 그룹"),
                                fieldWithPath("recommendActivity").type(JsonFieldType.STRING).description("추천 활동")
                        )
                ))
                .andExpect(status().isCreated());
    }


    @DisplayName("유저가 스크랩한 장소의 개수를 카운트한다.")
    @Test
    @WithUser
    void 유저가_스크랩한_장소개수를_스크랩타입별로_카운트한다() throws Exception {

        // given
        PlaceScrapCountResponseDto friendScrap = new PlaceScrapCountResponseDto(ScrapType.FRIEND, 1L);
        PlaceScrapCountResponseDto companyScrap = new PlaceScrapCountResponseDto(ScrapType.COMPANY, 1L);
        PlaceScrapCountResponseDto coupleScrap = new PlaceScrapCountResponseDto(ScrapType.COUPLE, 1L);
        PlaceScrapCountResponseDto familyScrap = new PlaceScrapCountResponseDto(ScrapType.FAMILY, 1L);


        given(placeScrapService.countByPlaceScrapType(any()))
                .willReturn(List.of(friendScrap, coupleScrap, companyScrap, familyScrap));

        // when
        mockMvc.perform(get("/api/users/place-scraps/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("user/place-scrap-count",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                                fieldWithPath("data.[].scrapType").type(JsonFieldType.STRING).description("스크랩 타입"),
                                fieldWithPath("data.[].scrapCount").type(JsonFieldType.NUMBER).description("장소 개수")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("유저가 작성한 메모리를 조회한다")
    @Test
    @WithUser
    void 유저가_작성한_메모리를_조회한다() throws Exception {

        // given

        given(memoryReadService.findMemoriesLoginUserWrite(anyLong(), any(Pageable.class), anyLong(), any(GroupType.class)))
                .willReturn(MemoryListResponse.from(List.of(MemoryResponseDto.builder().memoryId(1L).build()), true));

        // when
        mockMvc.perform(get("/api/users/my-memories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("user/my-memories",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                                fieldWithPath("data.[].contents").type(JsonFieldType.ARRAY).description("페이징 컨텐츠"),
                                fieldWithPath("data.[].next").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                fieldWithPath("data.[].contents.[].memoryId").type(JsonFieldType.NUMBER).description("메모리 ID"),
                                fieldWithPath("data.[].contents.[].imagePath").type(JsonFieldType.STRING).description("메모리 사진"),
                                fieldWithPath("data.[].contents.[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("data.[].contents.[].visitedDate").type(JsonFieldType.STRING).description("방문 일자"),
                                fieldWithPath("data.[].contents.[].groupType").type(JsonFieldType.STRING).description("그룹 타입")
                        )
                ))
                .andExpect(status().isOk());
    }
}