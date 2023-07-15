package cmc.mellyserver.mellyapi.comment.presentation.dto;

import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellycore.comment.application.dto.request.CommentRequestDto;

public abstract class CommentAssembler {

    public static CommentRequestDto commentRequestDto(Long userSeq, CommentRequest commentRequest) {
        return CommentRequestDto.builder()
                .userId(userSeq)
                .parentId(commentRequest.getParentId())
                .memoryId(commentRequest.getMemoryId())
                .content(commentRequest.getContent())
                .build();
    }

    private CommentAssembler() {
    }
}
