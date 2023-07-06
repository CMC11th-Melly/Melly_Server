package cmc.mellyserver.mellycommon.enums;

public enum UserStatus {
    ACTIVE("사용"),
    INACTIVE("휴면"),
    BLOCK("중지"),
    DELETE("탈퇴");

    private String type;

    UserStatus(String type) {
        this.type = type;
    }
}
