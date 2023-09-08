package cmc.mellyserver.mellyapi.common.response;

import cmc.mellyserver.mellyapi.common.code.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;


    public ApiResponse(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(final SuccessCode successCode, final T data) {
        return ResponseEntity.status(successCode.getStatus())
                .body(new ApiResponse<>(successCode.getStatus(), successCode.getMessage(), data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(final SuccessCode successCode) {
        return ResponseEntity.status(successCode.getStatus())
                .body(new ApiResponse<>(successCode.getStatus(), successCode.getMessage(), null));
    }
}
