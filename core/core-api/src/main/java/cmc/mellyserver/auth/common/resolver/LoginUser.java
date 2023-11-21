package cmc.mellyserver.auth.common.resolver;

public class LoginUser {

    private final Long userId;

    public LoginUser(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return userId;
    }

}
