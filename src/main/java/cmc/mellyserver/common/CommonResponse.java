package cmc.mellyserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;


    public CommonResponse(int code, String message)
    {
        this.code = code;
        this.message = message;
    }
}
