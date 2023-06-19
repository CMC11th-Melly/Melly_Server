package cmc.mellyserver.mellyappexternalapi.auth.application;

import cmc.mellyserver.mellydomain.common.enums.Provider;
import cmc.mellyserver.mellydomain.user.domain.User;

public interface LoginClient {

    boolean supports(Provider provider);

    User getUserData(String accessToken);
}
