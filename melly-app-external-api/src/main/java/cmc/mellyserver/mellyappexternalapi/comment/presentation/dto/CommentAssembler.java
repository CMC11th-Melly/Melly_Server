package cmc.mellyserver.mellyappexternalapi.comment.presentation.dto;

import cmc.mellyserver.mellyappexternalapi.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellyappexternalapi.comment.presentation.dto.request.CommentRequest;

public abstract class CommentAssembler {

	public static CommentRequestDto commentRequestDto(Long userSeq, CommentRequest commentRequest) {
		return CommentRequestDto.builder()
			.userId(userSeq)
			.parentId(commentRequest.getParentId())
			.memoryId(commentRequest.getMemoryId())
			.content(commentRequest.getContent())
			.mentionUserId(commentRequest.getMentionUserId())
			.build();
	}

	private CommentAssembler() {
	}
}