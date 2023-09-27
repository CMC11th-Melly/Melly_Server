package cmc.mellyserver.mellyapi.common.resolver;

import cmc.mellyserver.mellyapi.controller.auth.dto.common.CurrentUser;
import cmc.mellyserver.mellyapi.controller.auth.dto.common.LoginUser;
import cmc.mellyserver.mellyapi.common.token.TokenProvider;
import cmc.mellyserver.mellyapi.common.util.HeaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * SecurityContextHolder에서 유저 식별자 파싱하는 클래스
 */
@Component
@RequiredArgsConstructor
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = HeaderUtil.getAccessToken(request);
        Long id = tokenProvider.extractUserId(accessToken);
        return new LoginUser(id);
    }
}
