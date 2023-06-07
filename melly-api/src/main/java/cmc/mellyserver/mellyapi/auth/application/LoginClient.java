package cmc.mellyserver.mellyapi.auth.application;

import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;

public interface LoginClient {

    boolean supports(Provider provider);

    User getUserData(String accessToken);
}
