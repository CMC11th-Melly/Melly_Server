package cmc.mellyserver.mellyapi.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
	private int code;
	private String message;
	private T data;

	public CommonResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
