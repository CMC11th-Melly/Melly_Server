package cmc.mellyserver.auth.controller.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReIssueAccessTokenRequest {

  private String refreshToken;

}
