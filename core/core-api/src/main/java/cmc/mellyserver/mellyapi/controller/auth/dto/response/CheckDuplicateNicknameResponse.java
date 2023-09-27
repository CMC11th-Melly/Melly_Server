package cmc.mellyserver.mellyapi.controller.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckDuplicateNicknameResponse {

    private int code;
    private String message;

}
