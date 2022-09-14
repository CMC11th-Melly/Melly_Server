package cmc.mellyserver.auth.client;

import cmc.mellyserver.member.domain.Member;

public interface Client {

    public Member getUserData(String accessToken);
}
