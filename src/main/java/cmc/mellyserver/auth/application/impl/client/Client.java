package cmc.mellyserver.auth.application.impl.client;

import cmc.mellyserver.user.domain.User;

public interface Client {

    public User getUserData(String accessToken);
}
