package cmc.mellyserver.domain.notification.dto.response;

import lombok.Data;

@Data
public class NotificationOnOffResponseDto {

	private boolean enableAppPush;

	private boolean enableContentLike;

	private boolean enableContent;

	public NotificationOnOffResponseDto(boolean enableAppPush, boolean enableContent, boolean enableContentLike) {
		this.enableAppPush = enableAppPush;
		this.enableContent = enableContent;
		this.enableContentLike = enableContentLike;
	}

	public static NotificationOnOffResponseDto of(boolean enableAppPush, boolean enableContent,
			boolean enableContentLike) {
		return new NotificationOnOffResponseDto(enableAppPush, enableContent, enableContentLike);
	}

}
