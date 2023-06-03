package cmc.mellyserver.mellyapi.comment.presentation.dto.request;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

	private String content;
	private Long memoryId;
	@Nullable
	private Long parentId;
	@Nullable
	private Long mentionUserId;
}
