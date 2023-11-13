package cmc.mellyserver.controller.comment.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentUpdateRequest {

	private String content;

	public CommentUpdateRequest(String content) {
		this.content = content;
	}

}
