package cmc.mellyserver.mellydomain.common.enums;

public enum AgeGroup {
    ONE(10),
    TWO(20),
    THREE(30),
    FOUR(40),
    FIVE(50),
    SIX(60),
    SEVEN(70),
    DEFAULT(0);

    private int ageGroup;

//	TerminologyType(String typeName) {
//		this.typeName = typeName;
//	}
//
//	public static TerminologyType of(String typeName) {
//		return Arrays.stream(TerminologyType.values())
//				.filter(terminologyType -> terminologyType.getTypeName().equals(typeName))
//				.findFirst()
//				.orElseThrow(() -> new InvalidValueException(ErrorCode.TERMINOLOGY_TYPE_NOT_FOUND));
//	}
//}

    AgeGroup(int age) {
        this.ageGroup = age;
    }
}
