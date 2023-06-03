package cmc.mellyserver.mellyapi.comment.presentation.dto;

import cmc.mellyserver.mellyapi.comment.application.dto.CommentRequestDto;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;

public abstract class CommentAssembler {

	public static CommentRequestDto commentRequestDto(Long userSeq, CommentRequest commentRequest) {
		return CommentRequestDto.builder()
			.parentId(commentRequest.getParentId())
			.memoryId(commentRequest.getMemoryId())
			.content(commentRequest.getContent())
			.mentionUserId(commentRequest.getMentionUserId())
			.build();
	}
}
