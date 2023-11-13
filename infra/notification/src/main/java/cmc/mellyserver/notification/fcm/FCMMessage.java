package cmc.mellyserver.notification.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
class FCMMessage {

	private boolean validateOnly;

	private Message message;

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Message {

		private Notification notification;

		private String token;

	}

	@Builder
	@AllArgsConstructor
	@Getter
	public static class Notification {

		private String title;

		private String body;

		private String image;

	}

}
