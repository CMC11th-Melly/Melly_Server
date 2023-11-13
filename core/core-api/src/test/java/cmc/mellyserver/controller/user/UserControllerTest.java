package cmc.mellyserver.controller.user;

import cmc.mellyserver.support.ControllerTestSupport;

public class UserControllerTest extends ControllerTestSupport {

    //	@WithUser
    //	@DisplayName("회원의 프로필 정보를 조회한다.")
    //	@Test
    //	void 회원의_프로필정보를_조회한다() throws Exception {
    //
    //		// given
    //		given(userProfileService.getProfile(anyLong()))
    //			.willReturn(ProfileResponseDto.of(모카()));
    //		given(userProfileService.calculateImageTotalVolume(anyLong())).willReturn(1);
    //
    //		// when & then
    //		mockMvc
    //			.perform(get("/api/users/my-profile").accept(MediaType.APPLICATION_JSON)
    //				.contentType(MediaType.APPLICATION_JSON))
    //			.andDo(print())
    //			.andDo(document("user/profile", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
    //					responseFields(fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
    //							fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
    //							fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
    //							fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
    //							fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
    //							fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
    //							fieldWithPath("data.imageVolume").type(JsonFieldType.NUMBER).description("유저 이미지 총 용량"))))
    //			.andExpect(status().isOk());
    //	}

}