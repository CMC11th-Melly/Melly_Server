package cmc.mellyserver.controller.comment.dto;

import cmc.mellyserver.controller.comment.dto.request.CommentRequest;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;

public abstract class CommentAssembler {

	public static CommentRequestDto commentRequestDto(Long id, CommentRequest commentRequest) {
		return CommentRequestDto.builder()
			.userId(id)
			.mentionId(commentRequest.getMentionUserId())
			.rootId(commentRequest.getParentId())
			.memoryId(commentRequest.getMemoryId())
			.content(commentRequest.getContent())
			.build();
	}

	private CommentAssembler() {
	}

}
