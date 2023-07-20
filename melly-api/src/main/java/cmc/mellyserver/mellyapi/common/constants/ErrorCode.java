package cmc.mellyserver.mellyapi.common.constants;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    ;

    private final int status;

    private final String divisionCode;

    private final String message;

    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}