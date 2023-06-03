package cmc.mellyserver.mellyapi.comment.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentRequestDto {

	private String content;

	private Long memoryId;

	private Long parentId;

	private Long mentionUserId;

	@Builder
	public CommentRequestDto(String content, Long memoryId, Long parentId, Long mentionUserId) {
		this.content = content;
		this.memoryId = memoryId;
		this.parentId = parentId;
		this.mentionUserId = mentionUserId;
	}
}
