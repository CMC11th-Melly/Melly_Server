package cmc.mellyserver.mellyapi.common.response;

import cmc.mellyserver.mellycommon.codes.SuccessCode;
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

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessCode successCode, T data) {

        return ResponseEntity
                .status(successCode.getStatus())
                .body(new ApiResponse<>(successCode.getStatus(), successCode.getMessage(), data));
    }
}
