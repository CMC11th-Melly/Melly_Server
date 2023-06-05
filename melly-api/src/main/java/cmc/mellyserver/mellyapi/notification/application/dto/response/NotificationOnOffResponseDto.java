package cmc.mellyserver.mellyapi.notification.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class NotificationOnOffResponseDto {

	private boolean enableAppPush;
	private boolean enableContentLike;
	private boolean enableContent;

	@Builder
	public NotificationOnOffResponseDto(boolean enableAppPush, boolean enableContentLike, boolean enableContent) {
		this.enableAppPush = enableAppPush;
		this.enableContentLike = enableContentLike;
		this.enableContent = enableContent;
	}
}
