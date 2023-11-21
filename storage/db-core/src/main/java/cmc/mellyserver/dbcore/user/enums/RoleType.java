package cmc.mellyserver.dbcore.user.enums;

import lombok.Getter;

@Getter
public enum RoleType {

    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
