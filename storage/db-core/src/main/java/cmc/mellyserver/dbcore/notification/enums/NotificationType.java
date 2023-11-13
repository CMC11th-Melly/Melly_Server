package cmc.mellyserver.dbcore.notification.enums;

public enum NotificationType {

	COMMENT_ENROLL("댓글 등록"),
	COMMENT_LIKE("댓글 좋아요");

	private String description;

	NotificationType(String description) {
		this.description = description;
	}
}
