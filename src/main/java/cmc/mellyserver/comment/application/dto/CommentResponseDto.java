package cmc.mellyserver.comment.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private int commentCount;
    private List<CommentDto> comments;
}
