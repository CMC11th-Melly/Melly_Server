package cmc.mellyserver.mellycore.user.domain.enums;

public enum AgeGroup {
    ONE(10),
    TWO(20),
    THREE(30),
    FOUR(40),
    FIVE(50),
    SIX(60),
    SEVEN(70);

    private int ageGroup;

    public static AgeGroup from(final String value) {

        try {
            return AgeGroup.valueOf(value.toUpperCase());
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    AgeGroup(int age) {
        this.ageGroup = age;
    }
}