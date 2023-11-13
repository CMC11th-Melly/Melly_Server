package cmc.mellyserver.dbcore.memory;

public enum OpenType {

  ALL("전체 공개"),

  GROUP("그룹 공개"),

  PRIVATE("비공개");

  private String description;

  OpenType(String description) {
	this.description = description;
  }
}
