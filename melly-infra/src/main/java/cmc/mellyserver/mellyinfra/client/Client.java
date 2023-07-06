package cmc.mellyserver.mellyinfra.client;


import cmc.mellyserver.mellycore.user.domain.User;

public interface Client {

    public User getUserData(String accessToken);
}
