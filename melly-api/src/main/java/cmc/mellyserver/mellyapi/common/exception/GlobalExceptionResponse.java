package cmc.mellyserver.mellyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GlobalExceptionResponse {

	private String code;
	private String message;
}
