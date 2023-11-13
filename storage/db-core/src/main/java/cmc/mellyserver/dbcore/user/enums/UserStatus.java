package cmc.mellyserver.dbcore.user.enums;

public enum UserStatus {

  ACTIVE("활성화"),

  INACTIVE("휴면"),

  BLOCK("차단"),

  DELETE("탈퇴");

  private String type;

  UserStatus(String type) {
	this.type = type;
  }

}
