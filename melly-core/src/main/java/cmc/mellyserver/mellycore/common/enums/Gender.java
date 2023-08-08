package cmc.mellyserver.mellycore.common.enums;

public enum Gender {
    MALE, FEMALE;

    public static Gender from(final String value) {
        try {
            return Gender.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
