package cmc.mellyserver.domain.comment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.domain.comment.dto.response.CommentResponseDto;
import cmc.mellyserver.domain.comment.event.CommentEnrollEvent;
import cmc.mellyserver.domain.memory.MemoryReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final MemoryReader memoryReader;

	private final CommentReader commentReader;

	private final CommentWriter commentWriter;

	private final ApplicationEventPublisher publisher;

	@Transactional(readOnly = true)
	public CommentResponseDto getComments(final Long userId, final Long memoryId) {
		return commentReader.findByMemoryId(userId, memoryId);
	}

	@Transactional
	public Comment saveComment(final CommentRequestDto commentRequestDto) {
		Memory memory = memoryReader.findById(commentRequestDto.getMemoryId());
		Comment comment = commentWriter.save(commentRequestDto);
		publisher.publishEvent(
			new CommentEnrollEvent(memory.getUserId(), comment.getMemoryId(), comment.getUser().getNickname()));
		return comment;
	}

	@Transactional
	public Comment updateComment(final Long commentId, final String content) {
		Comment comment = commentReader.findById(commentId);
		comment.update(content);
		return comment;
	}

	@Transactional
	public void deleteComment(Long commentId) {
		Comment comment = commentReader.findById(commentId);
		commentWriter.remove(comment);
	}
}
