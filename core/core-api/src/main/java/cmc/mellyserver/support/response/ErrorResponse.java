package cmc.mellyserver.support.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import cmc.mellyserver.support.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int status;

    private final String code;

    private final String message;

    private List<FieldError> errors;

    private String reason;

    @Builder
    protected ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.code = code.getCode();
    }

    @Builder
    protected ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }

    @Builder
    protected ErrorResponse(final ErrorCode code, final String reason) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.reason = reason;
    }

    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    @Getter
    public static class FieldError {

        private final String field;

        private final String value;

        private final String reason;

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                .map(error -> new FieldError(error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()))
                .collect(Collectors.toList());
        }

    }

}
