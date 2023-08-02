package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReIssueAccessTokenRequest {

    private String refreshToken;
}
