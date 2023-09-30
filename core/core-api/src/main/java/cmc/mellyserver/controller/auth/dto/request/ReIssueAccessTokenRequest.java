package cmc.mellyserver.controller.auth.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReIssueAccessTokenRequest {

    private String refreshToken;
}
