package cmc.mellyserver.domain.comment;

import static java.lang.Boolean.*;

import java.util.Objects;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentWriter {

	private final CommentRepository commentRepository;

	private final UserReader userReader;

	private final MemoryReader memoryReader;

	private final CommentReader commentReader;

	public Comment save(CommentRequestDto commentRequestDto) {

		Comment parentComment = findParent(commentRequestDto.getParentId());
		return saveComment(parentComment, commentRequestDto);
	}

	public void remove(Comment comment) {
		removeCommentAccordingToChildComment(comment);
	}

	private Comment saveComment(Comment parentComment, CommentRequestDto commentRequestDto) {

		User user = userReader.findById(commentRequestDto.getUserId());
		Memory memory = memoryReader.findById(commentRequestDto.getMemoryId());

		if (Objects.isNull(parentComment)) {
			return commentRepository.save(
				Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), null));
		}

		Comment comment = commentRepository.save(
			Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), parentComment));
		parentComment.getChildren().add(comment);
		return comment;
	}

	private Comment findParent(Long parentId) {

		if (Objects.isNull(parentId)) {
			return null;
		}

		return commentReader.findById(parentId);
	}

	private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함

		Comment parent = comment.getParent();
		if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == TRUE) {
			return getDeletableAncestorComment(parent);
		}
		return comment;
	}

	private void removeCommentAccordingToChildComment(Comment comment) {

		// 만약 자식이 없다면
		if (comment.getChildren().isEmpty()) {
			Comment deletableAncestorComment = getDeletableAncestorComment(comment);
			deletableAncestorComment.delete();
			comment.delete();
		} else {
			comment.remove();
		}
	}
}
