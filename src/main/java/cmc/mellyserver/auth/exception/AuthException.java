package cmc.mellyserver.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException{

    protected AuthException(String message) {
        super(message);
    }
}
