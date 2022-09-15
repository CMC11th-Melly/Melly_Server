package cmc.mellyserver.auth.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthRequest {

    private String accessToken;
    private SnsType snsType;
}