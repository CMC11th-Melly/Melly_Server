package cmc.mellyserver.mellyapi.common.event;

import org.springframework.context.ApplicationEvent;

import cmc.mellyserver.mellycore.common.enums.NotificationType;
import lombok.Getter;

@Getter
public class CustomEvent extends ApplicationEvent {

	private NotificationType notificationType;
	private String message;
	private String fcmToken;
	private String uid;
	private Long memoryId;

	public CustomEvent(Object source, String fcmToken, NotificationType notificationType, String message, String uid,
		Long memoryId) {
		super(source);
		this.notificationType = notificationType;
		this.message = message;
		this.fcmToken = fcmToken;
		this.uid = uid;
		this.memoryId = memoryId;
	}
}
