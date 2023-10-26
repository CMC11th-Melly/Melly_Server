package cmc.mellyserver.controller.user;

import static cmc.mellyserver.common.fixture.UserFixtures.모카;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cmc.mellyserver.controller.ControllerTest;
import cmc.mellyserver.controller.common.annotation.WithUser;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class UserControllerTest extends ControllerTest {

	@WithUser
	@DisplayName("회원의 프로필 정보를 조회한다.")
	@Test
	void 회원의_프로필정보를_조회한다() throws Exception {

		// given
		given(userProfileService.getProfile(anyLong()))
			.willReturn(ProfileResponseDto.of(1L, 모카().getNickname(), 모카().getEmail(), 모카().getProfileImage()));
		given(userProfileService.checkImageStorageVolume(anyLong())).willReturn(1);

		// when & then
		mockMvc
			.perform(get("/api/users/my-profile").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andDo(document("user/profile", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
					responseFields(fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
							fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
							fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
							fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
							fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
							fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
							fieldWithPath("data.imageVolume").type(JsonFieldType.NUMBER).description("유저 이미지 총 용량"))))
			.andExpect(status().isOk());

	}

}
