package cmc.mellyserver.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        // 매개변수로 받은 code가 enum 안에 없으면 GUEST권한을 줌. 찾으면 그 권한 반환
        return Arrays.stream(RoleType.values())
                .filter(role -> role.getCode().equals(code))
                .findAny()
                .orElse(GUEST);
    }
}
