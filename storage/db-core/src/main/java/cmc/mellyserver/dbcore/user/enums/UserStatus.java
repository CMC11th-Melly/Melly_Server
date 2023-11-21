package cmc.mellyserver.dbcore.user.enums;

public enum UserStatus {

    ACTIVE("활성화"),

    INACTIVE("휴면"),

    BLOCK("차단"),

    DELETE("탈퇴");

    public String getDescription() {
        return description;
    }

    private String description;

    UserStatus(String description) {
        this.description = description;
    }

}
