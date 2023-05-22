package cmc.mellyserver.unit.user;

import cmc.mellyserver.common.WithUser;
import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.unit.ControllerTest;
import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UserControllerTest extends ControllerTest {

    // 중첩 테스트 시도하기
    @DisplayName("로그인이 되어있을 때")
    @Nested
    class Test_UnderLoginStatus{

        @DisplayName("유저 식별자를 통해 유저 정보를 가져올 수 있다.")
        @Test
        @WithUser
        void getAuthenticatedUserProfile() throws Exception {

            // given
            given(userService.findNicknameByUserIdentifier(1L)).willReturn("test nickname");

            // when
            ResultActions perform  = mockMvc.perform(get("/api/user/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.data").value("test nickname"))
                            .andExpect(jsonPath("$.code").value(200))
                                    .andExpect(jsonPath("$.message").value("성공"));

            verify(userService,times(1))
                    .findNicknameByUserIdentifier(anyLong());
        }

        @DisplayName("설문 조사를 진행하면 설문 조사 결과를 알려준다.")
        @Test
        @WithUser
        void getSurveyResultForSignupUser() throws Exception
        {
            // given
            SurveyRecommendResponseDto surveyRecommendResponseDto = new SurveyRecommendResponseDto(new Position(100.0, 100.0), List.of("test_word_1", "test_word_2"));
            given(userService.getSurveyResult(1L)).willReturn(surveyRecommendResponseDto);

            // when
            ResultActions perform  = mockMvc.perform(get("/api/user/survey")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.data.words", Matchers.contains("test_word_1","test_word_2")));

            verify(userService,times(1))
                    .getSurveyResult(anyLong());
        }

        @DisplayName("유저 프로필을 수정할 수 있는 데이터를 조회한다.")
        @Test
        @WithUser
        void getUpdateProfileData() throws Exception
        {
            // given
            ProfileUpdateFormResponseDto profileUpdateFormResponseDto = new ProfileUpdateFormResponseDto("test_image", "test_nickname", Gender.MALE, AgeGroup.TWO);
            given(userService.getLoginUserProfileDataForUpdate(1L)).willReturn(profileUpdateFormResponseDto);

            // when
            ResultActions perform  = mockMvc.perform(get("/api/user/profile")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));

            verify(userService,times(1))
                    .getLoginUserProfileDataForUpdate(anyLong());
        }


        @DisplayName("마이페이지에서 로그인한 사용자가 작성한 메모리 목록을 확인할 수 있다.")
        @Test
        @WithUser
        void getMemoriesLoginUserCreated() throws Exception
        {
            // given
//            given(userService.getLoginUserProfileDataForUpdate(1L)).willReturn(profileUpdateFormResponseDto);

            // when
            ResultActions perform  = mockMvc.perform(get("/api/user/profile")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.message").value("성공"));


            verify(userService,times(1))
                    .getLoginUserProfileDataForUpdate(anyLong());
        }



    }
}
