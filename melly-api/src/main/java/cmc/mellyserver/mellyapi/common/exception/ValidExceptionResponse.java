package cmc.mellyserver.mellyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidExceptionResponse {

	private String code;
	private String message;
}
