package cmc.mellyserver.mellyapi.support.exception;

public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
