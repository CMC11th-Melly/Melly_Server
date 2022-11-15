package cmc.mellyserver.common.exception;

import lombok.Getter;

@Getter
public class GlobalBadRequestException extends RuntimeException{

    private final ExceptionCodeAndDetails exceptionCodeAndDetails;

    public GlobalBadRequestException(ExceptionCodeAndDetails exceptionCodeAndDetails)
    {
        super(exceptionCodeAndDetails.getMessage());
        this.exceptionCodeAndDetails = exceptionCodeAndDetails;
    }
}
