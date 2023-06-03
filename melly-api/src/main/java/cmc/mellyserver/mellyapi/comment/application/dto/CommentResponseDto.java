package cmc.mellyserver.mellyapi.comment.application.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
	private int commentCount;

	private List<CommentDto> comments;
}
