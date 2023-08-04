package cmc.mellyserver.mellyapi.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse> OK(T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "OK", data));
    }

    public static <T> ResponseEntity<ApiResponse> OK() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "OK"));
    }

    public static <T> ResponseEntity<ApiResponse> CREATE() {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), "OK"));
    }
}
