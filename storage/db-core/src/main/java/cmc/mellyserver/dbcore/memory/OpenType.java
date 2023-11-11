package cmc.mellyserver.dbcore.memory;

public enum OpenType {

	ALL("전체 공개"),

	GROUP("그룹 공개"),

	PRIVATE("비공개");

	private String name;

	OpenType(String name) {
		this.name = name;
	}
}
