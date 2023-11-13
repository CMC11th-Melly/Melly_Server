package cmc.mellyserver.auth.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangePasswordRequest {

    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    private String passwordAfter;

    private String passwordBefore;

    @Builder
    public ChangePasswordRequest(String email, String passwordAfter, String passwordBefore) {
        this.email = email;
        this.passwordAfter = passwordAfter;
        this.passwordBefore = passwordBefore;
    }

}
