package cmc.mellyserver.auth.presentation;

import cmc.mellyserver.auth.application.AuthService;
import cmc.mellyserver.auth.application.OAuthService;
import cmc.mellyserver.auth.config.SecurityConfig;
import cmc.mellyserver.auth.presentation.dto.*;
import cmc.mellyserver.auth.presentation.dto.request.AuthRequestForLogin;
import cmc.mellyserver.auth.presentation.dto.request.CheckDuplicateEmailRequest;
import cmc.mellyserver.auth.presentation.dto.request.CheckDuplicateNicknameRequest;
import cmc.mellyserver.auth.presentation.dto.response.AccessTokenUserData;
import cmc.mellyserver.auth.presentation.dto.response.AuthResponseForLogin;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.exception.GlobalExceptionHandler;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest(controllers = AuthController.class,
		excludeFilters = {
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OAuthService oAuthService;

    @MockBean
    AuthService authService;

    @MockBean
    AuthenticatedUserChecker authenticatedUserChecker;


    @Test
    @DisplayName("이메일 로그인 테스트")
    @WithMockUser
    void email_login_test() throws Exception {
        AuthRequestForLogin request = createRequestForEmailLogin();
        AuthResponseForLogin response = createResponseForEmailLogin();

        BDDMockito.doReturn(response).when(authService)
                .login(BDDMockito.anyString(),BDDMockito.anyString(),BDDMockito.anyString());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login").with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new Gson().toJson(request))
        );

       resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.token").value("access_token"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("로그인 완료"));

    }

    @Test
    @DisplayName("중복 이메일이 들어오면 400 예외 발생")
    @WithMockUser
    void duplicated_email() throws Exception {

        CheckDuplicateEmailRequest request = new CheckDuplicateEmailRequest("melly@gmail.com");

        BDDMockito.doThrow(new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_EMAIL))
                .when(authService).checkDuplicatedEmail(BDDMockito.anyString());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/email").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("중복되는 이메일입니다."));
    }

    @Test
    @DisplayName("중복되지 않은 이메일이면 200 반환")
    @WithMockUser
    void not_duplicated_email() throws Exception {

        CheckDuplicateEmailRequest request = new CheckDuplicateEmailRequest("melly@gmail.com");

        BDDMockito.doNothing().when(authService).checkDuplicatedEmail(BDDMockito.anyString());
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/email").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("사용해도 좋은 이메일입니다."));
    }


    @Test
    @DisplayName("중복 닉네임이 들어오면 400 예외 발생")
    @WithMockUser
    void duplicated_nickname() throws Exception {

        CheckDuplicateNicknameRequest request = new CheckDuplicateNicknameRequest("모카");

        BDDMockito.doThrow(new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_NICKNAME))
                .when(authService).checkNicknameDuplicate(BDDMockito.anyString());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/nickname").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1001"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("중복되는 닉네임입니다."));
    }

    @Test
    @DisplayName("중복되지 않은 닉네임이면 200 반환")
    @WithMockUser
    void not_duplicated_nickname() throws Exception {

        CheckDuplicateEmailRequest request = new CheckDuplicateEmailRequest("모카");

        BDDMockito.doNothing().when(authService).checkNicknameDuplicate(BDDMockito.anyString());
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/nickname").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(request)));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("사용해도 좋은 닉네임입니다."));
    }

    @Test
    @DisplayName("로그아웃 테스트 성공")
    @WithMockUser
    void logout() throws Exception {

        String uid = SecurityContextHolder.getContext().getAuthentication().getName();
        BDDMockito.doNothing().when(authService).logout(BDDMockito.anyString(),BDDMockito.anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/auth/logout").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("로그아웃 완료"));
    }

    @Test
    @DisplayName("DB에 인증 사용자 정보가 없으면 예외 발생")
    @WithMockUser(username = "mellyLoginUser")
    void logout_not_exist_user() throws Exception {

        BDDMockito.doThrow(new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER)).when(authService).logout(BDDMockito.anyString(),BDDMockito.anyString());
        mockMvc.perform(MockMvcRequestBuilders.delete("/auth/logout").with(csrf()).header("Authorization","Bearer accessToken"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value("1004"));
    }

    @Test
    @DisplayName("일반 이메일 회원가입 성공")
    @WithMockUser
    void signup() throws Exception {

        final String fileName = "testimage";
        final String contentType = "jpg";
        final String filePath = "src/test/resources/image/"+fileName+"."+contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile image1 = new MockMultipartFile(
                "images", //name
                fileName + "." + contentType,
                contentType,
                fileInputStream
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/auth/signup")
                .file(image1)
                .param("nickname","모카")
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());





    }







    private AuthResponseForLogin createResponseForEmailLogin()
   {
       return AuthResponseForLogin.builder().token("access_token")
               .user(new AccessTokenUserData(1L,
                       "uuid", Provider.DEFAULT,
                       "melly@gmail.com",
                       "떡잎마을방범대",
                       "cmc11th.jpg",
                       Gender.MALE,
                       AgeGroup.TWO)).build();
   }



    private AuthRequestForLogin createRequestForEmailLogin() {
        return AuthRequestForLogin.builder()
                                                .email("melly@gmail.com")
                                                .password("asdfasdf")
                .fcmToken("dfdfdfd")
                                                .build();

    }
}