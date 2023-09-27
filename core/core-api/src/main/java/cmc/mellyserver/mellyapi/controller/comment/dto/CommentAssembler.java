package cmc.mellyserver.mellyapi.controller.comment.dto;

import cmc.mellyserver.mellyapi.controller.comment.dto.request.CommentRequest;
import cmc.mellyserver.mellyapi.domain.comment.dto.request.CommentRequestDto;

public abstract class CommentAssembler {

    public static CommentRequestDto commentRequestDto(Long id, CommentRequest commentRequest) {
        return CommentRequestDto.builder()
                .userId(id)
                .parentId(commentRequest.getParentId())
                .memoryId(commentRequest.getMemoryId())
                .content(commentRequest.getContent())
                .build();
    }

    private CommentAssembler() {
    }
}
