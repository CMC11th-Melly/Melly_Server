package cmc.mellyserver.clientauth;

import cmc.mellyserver.clientauth.api.LoginClientResult;

public interface LoginClient {

	static final String AUTH_PREFIX = "Bearer ";

	boolean supports(String provider);

	LoginClientResult getUserData(String accessToken);

}
