package cmc.mellyserver.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalBadRequestException.class)
    public ResponseEntity<GlobalExceptionResponse> badRequestException(GlobalBadRequestException e)
    {
        return ResponseEntity.badRequest().body(new GlobalExceptionResponse(e.getExceptionCodeAndDetails().getCode(),e.getExceptionCodeAndDetails().getMessage()));
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
                .body(new GlobalExceptionResponse(ExceptionCodeAndDetails.BAD_REQUEST.getCode(), e.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<GlobalExceptionResponse> handleError404() {
        String code = ExceptionCodeAndDetails.NOT_FOUND_API.getCode();
        String message = ExceptionCodeAndDetails.NOT_FOUND_API.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GlobalExceptionResponse(code, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidExceptionResponse> handlerBeanValidation(MethodArgumentNotValidException e)
    {
        String code = "333";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidExceptionResponse(code,e.getBindingResult().toString()));
    }



    @ExceptionHandler(GlobalServerException.class)
    public ResponseEntity<Void> serverException() {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
