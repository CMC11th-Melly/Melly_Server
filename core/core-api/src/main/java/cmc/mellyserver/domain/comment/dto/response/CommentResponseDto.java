package cmc.mellyserver.domain.comment.dto.response;

import java.util.List;

import lombok.Builder;

public record CommentResponseDto(int count, List<CommentDto> comments) {

    @Builder
    public CommentResponseDto {
    }
}
