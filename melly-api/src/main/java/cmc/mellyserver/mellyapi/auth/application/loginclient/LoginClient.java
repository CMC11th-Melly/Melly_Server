package cmc.mellyserver.mellyapi.auth.application.loginclient;

import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;

public interface LoginClient {

    static final String AUTH_PREFIX = "Bearer ";

    boolean supports(Provider provider);

    User getUserData(String accessToken);
}
