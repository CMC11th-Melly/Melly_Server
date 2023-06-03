package cmc.mellyserver.mellyapi.auth.application.impl.client;

import cmc.mellyserver.mellycore.user.domain.User;

public interface Client {

	public User getUserData(String accessToken);
}
