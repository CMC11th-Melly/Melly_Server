package cmc.mellyserver.auth.config;

public abstract class WhiteList {

    public static final String[] WHITE_LIST = {"/actuator/**", "/auth/social/signup", "/api/auth/social",
        "/api/auth/signup", "/api/auth/login", "/api/auth/token/reissue", "/auth/nickname", "/auth/email",
        "/api/health", "/api/auth/email-certification/sends", "/api/auth/email-certification/resends",
        "/api/auth/email-certification/confirms", "/api/auth/social-signup", "/"};

}
