package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponse {

    private String appToken;
}
