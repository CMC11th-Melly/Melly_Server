package cmc.mellyserver.support.response;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

  private int code;

  private String message;

  private T data;

  public static <T> ResponseEntity<ApiResponse<T>> success(final SuccessCode successCode, final T data) {
	return ResponseEntity.status(successCode.getStatus())
		.body(new ApiResponse<>(successCode.getStatus(), successCode.getMessage(), data));
  }

  public static <T> ResponseEntity<ApiResponse<T>> success(final SuccessCode successCode) {
	return ResponseEntity.status(successCode.getStatus())
		.body(new ApiResponse<>(successCode.getStatus(), successCode.getMessage(), null));
  }

}
