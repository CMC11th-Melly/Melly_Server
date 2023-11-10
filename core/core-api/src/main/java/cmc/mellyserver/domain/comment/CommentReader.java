package cmc.mellyserver.domain.comment;

import static java.lang.Boolean.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.dto.response.CommentDto;
import cmc.mellyserver.domain.comment.dto.response.CommentResponseDto;
import cmc.mellyserver.domain.comment.query.CommentQueryRepository;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentReader {

	private final CommentRepository commentRepository;

	private final UserReader userReader;

	private final CommentQueryRepository commentQueryRepository;

	public Comment findById(final Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(() -> {
			throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
		});
	}

	public CommentResponseDto findByMemoryId(final Long userId, final Long memoryId) {
		User user = userReader.findById(userId);
		List<Comment> comment = commentQueryRepository.findComment(userId, memoryId);
		return convertNestedStructure(comment, user);
	}

	private CommentResponseDto convertNestedStructure(List<Comment> comments, User user) {

		// CommentDto를 닮을 리스트
		List<CommentDto> result = new ArrayList<>();
		Map<Long, CommentDto> map = new HashMap<>();

		// 삭제 되지 않은 리스트 개수
		int cnt = (int)comments.stream().filter(c -> c.getIsDeleted().equals(FALSE)).count();

		comments.forEach(comment -> {

			CommentDto dto = CommentDto.convertCommentToDto(comment, user);

			// 그 댓글을 map에 넣고
			map.put(dto.getId(), dto);

			// 만약 부모가 있다면
			if (Objects.nonNull(comment.getParent())) {

				if (map.getOrDefault(comment.getParent().getId(), null) != null) {
					map.get(comment.getParent().getId()).getChildren().add(dto);
				}

			} else {
				result.add(dto);
			}
		});
		return new CommentResponseDto(cnt, result);
	}

}
