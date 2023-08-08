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


}