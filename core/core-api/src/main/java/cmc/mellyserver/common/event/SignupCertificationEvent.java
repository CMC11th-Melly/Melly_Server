package cmc.mellyserver.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupCertificationEvent {

    private String email;

    private String content;
}
