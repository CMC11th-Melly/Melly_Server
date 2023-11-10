package cmc.mellyserver.clientauth;

import cmc.mellyserver.clientauth.api.LoginClientResult;

public interface LoginClient {

	boolean supports(String provider);

	LoginClientResult getUserData(String accessToken);
}
