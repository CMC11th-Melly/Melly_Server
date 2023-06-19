package cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.request;

import org.springframework.lang.Nullable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

	private String content;
	private Long memoryId;
	@Nullable
	private Long parentId;
	@Nullable
	private Long mentionUserId;

	@Builder
	public CommentRequest(String content, Long memoryId, @Nullable Long parentId, @Nullable Long mentionUserId) {
		this.content = content;
		this.memoryId = memoryId;
		this.parentId = parentId;
		this.mentionUserId = mentionUserId;
	}
}
