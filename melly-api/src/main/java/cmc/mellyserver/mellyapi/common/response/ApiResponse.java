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

    private static final String SUCCESS_MESSAGE = "성공";

    private int code;
    private String message;
    private T data;


    public ApiResponse(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse> OK(final T data) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, data));
    }
}
