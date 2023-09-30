package cmc.mellyserver.common.code;

import lombok.Getter;


@Getter
public enum SuccessCode {

    SELECT_SUCCESS(200, "SELECT SUCCESS"),
    INSERT_SUCCESS(201, "INSERT SUCCESS"),
    DELETE_SUCCESS(200, "DELETE SUCCESS"),
    UPDATE_SUCCESS(200, "UPDATE SUCCESS");

    private final int status;
    private final String message;

    SuccessCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }
}