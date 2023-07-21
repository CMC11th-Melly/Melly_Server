package cmc.mellyserver.mellycore.common.exception;

import cmc.mellyserver.mellycommon.codes.ErrorCode;

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
