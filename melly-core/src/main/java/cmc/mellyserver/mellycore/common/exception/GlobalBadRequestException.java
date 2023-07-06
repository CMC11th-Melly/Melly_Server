package cmc.mellyserver.mellycore.common.exception;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalBadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalBadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
