package cmc.mellyserver.common.exception;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException()
    {
        super("유효하지 않은 토큰입니다.");
    }

    public TokenValidFailedException(String message)
    {
        super(message);
    }

}
