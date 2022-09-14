package cmc.mellyserver.auth.exception.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    String errorCode;
    String message;
}
