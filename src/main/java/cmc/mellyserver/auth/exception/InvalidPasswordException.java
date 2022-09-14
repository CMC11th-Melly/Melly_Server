package cmc.mellyserver.auth.exception;

public class InvalidPasswordException extends AuthException{
    public InvalidPasswordException()
    {
        super("일치하지 않는 비밀번호입니다.");
    }

    public InvalidPasswordException(String message)
    {
        super(message);
    }
}
