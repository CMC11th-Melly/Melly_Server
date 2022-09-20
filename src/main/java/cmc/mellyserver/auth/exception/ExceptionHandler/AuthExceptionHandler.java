package cmc.mellyserver.auth.exception.ExceptionHandler;

import cmc.mellyserver.auth.exception.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorMessage> authException(AuthException ex)
    {
        String message = ex.getMessage();
        return ResponseEntity.ok(new ErrorMessage(400,message));
    }
}
