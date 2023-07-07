package cmc.mellyserver.mellyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    private String code;
    private String message;
}
