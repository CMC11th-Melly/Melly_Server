package cmc.mellyserver.auth.exception.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    int errorCode;
    String message;
}
