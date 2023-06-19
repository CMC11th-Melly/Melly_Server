package cmc.mellyserver.mellyappexternalapi.comment.application.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

	private Long userId;

	private String content;

	private Long memoryId;

	private Long parentId;

	private Long mentionUserId;

	@Builder
	public CommentRequestDto(Long userId, String content, Long memoryId, Long parentId, Long mentionUserId) {
		this.userId = userId;
		this.content = content;
		this.memoryId = memoryId;
		this.parentId = parentId;
		this.mentionUserId = mentionUserId;
	}
}
