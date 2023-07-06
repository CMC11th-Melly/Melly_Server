package cmc.mellyserver.mellyapi.common.exception;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalBadRequestException.class)
    public ResponseEntity<GlobalExceptionResponse> badRequestException(GlobalBadRequestException e) {
        return ResponseEntity.badRequest()
                .body(new GlobalExceptionResponse(e.getErrorCode().getCode(),
                        e.getErrorCode().getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            BindException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<GlobalExceptionResponse> badRequest(Exception e) {

        return ResponseEntity.badRequest()
                .body(new GlobalExceptionResponse(ErrorCode.BAD_REQUEST.getCode(), e.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GlobalExceptionResponse> handleError404() {
        String code = ErrorCode.NOT_FOUND_API.getCode();
        String message = ErrorCode.NOT_FOUND_API.getCode();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GlobalExceptionResponse(code, message));
    }

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<ValidExceptionResponse> handlerBeanValidation(
            org.springframework.validation.BindException e) {
        String code = "유효성 검증 실패";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidExceptionResponse(code, e.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(GlobalServerException.class)
    public ResponseEntity<Void> serverException() {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
