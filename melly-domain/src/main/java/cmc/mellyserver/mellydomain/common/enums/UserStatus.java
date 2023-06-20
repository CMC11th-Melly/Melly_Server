package cmc.mellyserver.mellydomain.common.enums;

public enum UserStatus {
    ACTIVE("사용"),
    INACTIVE("휴면"),
    BLOCK("중지");

    private String type;

    UserStatus(String type) {
        this.type = type;
    }
}
