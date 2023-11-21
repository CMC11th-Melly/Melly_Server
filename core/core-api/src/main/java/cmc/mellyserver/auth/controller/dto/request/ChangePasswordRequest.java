package cmc.mellyserver.auth.controller.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public record ChangePasswordRequest(

    String email,

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,}", message = "비밀번호는 영문과 숫자 조합으로 8자리 이상 가능합니다.")
    String passwordAfter,

    String passwordBefore

) {
    @Builder
    public ChangePasswordRequest {
    }
}
