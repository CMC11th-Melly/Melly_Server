package cmc.mellyserver.mellycommon.codes;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    SELECT_SUCCESS(HttpStatus.OK.value(), "200", "SELECT SUCCESS"),

    DELETE_SUCCESS(HttpStatus.OK.value(), "200", "DELETE SUCCESS"),

    LOGIN_SUCCESS(HttpStatus.OK.value(), "200", "LOGIN SUCCESS"),

    INSERT_SUCCESS(HttpStatus.CREATED.value(), "201", "INSERT SUCCESS"),

    UPDATE_SUCCESS(HttpStatus.NO_CONTENT.value(), "204", "UPDATE SUCCESS"),

    ;

    private final int status;

    private final String code;

    private final String message;

    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
