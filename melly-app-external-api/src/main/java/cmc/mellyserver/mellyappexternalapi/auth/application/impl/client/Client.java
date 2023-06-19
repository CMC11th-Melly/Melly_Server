package cmc.mellyserver.mellyappexternalapi.auth.application.impl.client;

import cmc.mellyserver.mellydomain.user.domain.User;

public interface Client {

    public User getUserData(String accessToken);
}
