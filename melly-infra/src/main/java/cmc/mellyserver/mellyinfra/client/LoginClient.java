package cmc.mellyserver.mellyinfra.client;

import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;

public interface LoginClient {

    boolean supports(Provider provider);

    User getUserData(String accessToken);
}
