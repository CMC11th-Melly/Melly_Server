package cmc.mellyserver.clientauth;


import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;

public interface LoginClient {

    static final String AUTH_PREFIX = "Bearer ";

    boolean supports(Provider provider);

    User getUserData(String accessToken);
}
