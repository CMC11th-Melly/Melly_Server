package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckDuplicateEmailResponse {

    private int code;
    private String message;

}
