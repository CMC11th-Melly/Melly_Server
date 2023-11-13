package cmc.mellyserver.auth.certification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCertificationRequest {

  private String email;

  private String certificationNumber;

}
