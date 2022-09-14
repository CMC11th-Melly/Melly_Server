package cmc.mellyserver.auth.exception;

public class TokenValidFailedException extends AuthException{

    public TokenValidFailedException()
    {
        super("유효하지 않은 토큰입니다.");
    }

    public TokenValidFailedException(String message)
    {
        super(message);
    }

}
