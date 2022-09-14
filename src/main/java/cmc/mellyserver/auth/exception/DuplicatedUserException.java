package cmc.mellyserver.auth.exception;

public class DuplicatedUserException extends AuthException{

    public DuplicatedUserException()
    {
        super("중복 회원가입입니다.");
    }


    public DuplicatedUserException(String message)
    {
        super(message);
    }

}
