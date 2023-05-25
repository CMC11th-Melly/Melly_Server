package cmc.mellyserver.auth.application;


import cmc.mellyserver.common.enums.Provider;
import cmc.mellyserver.user.domain.User;

public interface LoginClient {

    boolean supports(Provider provider);
    User getUserData(String accessToken);
}
