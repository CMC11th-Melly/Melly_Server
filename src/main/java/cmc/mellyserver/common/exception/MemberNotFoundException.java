package cmc.mellyserver.common.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException()
    {
        super("존재하지 않는 유저입니다.");
    }

    public MemberNotFoundException(String message)
    {
        super(message);
    }
}
