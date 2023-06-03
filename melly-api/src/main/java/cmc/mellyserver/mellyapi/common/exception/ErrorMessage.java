package cmc.mellyserver.mellyapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

	int errorCode;
	String message;
}
