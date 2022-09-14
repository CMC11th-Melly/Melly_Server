package cmc.mellyserver.auth.exception;

public class InvalidEmailException extends AuthException{

    public InvalidEmailException()
    {
        super("유효하지 않은 이메일입니다.");
    }

    public InvalidEmailException(String message)
    {
        super(message);
    }
}
