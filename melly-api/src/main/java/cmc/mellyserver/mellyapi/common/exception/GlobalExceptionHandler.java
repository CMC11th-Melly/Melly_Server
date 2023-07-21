package cmc.mellyserver.mellyapi.common.exception;


import cmc.mellyserver.mellyapi.common.response.ErrorResponse;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            BindException.class,
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> badRequest(Exception e) {

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 비즈니스 예외 일괄 처리
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    // 잡히지 않은 에러들을 일괄 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
