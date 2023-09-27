package cmc.mellyserver.mellyapi.controller.auth.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class LogoutRequest {

    private String accessToken;
}
