package cmc.mellyserver.auth.application.impl.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoogleUserData {

    private String sub;
    private String email;
    private Boolean email_verified;

}
