package cmc.mellyserver.mellyapi.auth.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckDuplicateNicknameRequest {

    private String nickname;

    public CheckDuplicateNicknameRequest(String nickname) {
        this.nickname = nickname;
    }
}
