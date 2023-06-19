package cmc.mellyserver.mellyappexternalapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

	int errorCode;
	String message;
}
