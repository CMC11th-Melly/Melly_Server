package cmc.mellyserver.mellyapi.comment.application.impl;

import static cmc.mellyserver.mellyapi.comment.application.dto.response.CommentDto.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.mellyapi.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellyapi.comment.application.dto.response.CommentDto;
import cmc.mellyserver.mellyapi.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellyapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.common.enums.DeleteStatus;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl {

	private final CommentQueryRepository commentQueryRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final AuthenticatedUserChecker authenticatedUserChecker;
	private final CommentLikeRepository commentLikeRepository;
	private final MemoryRepository memoryRepository;

	public CommentResponseDto getComment(Long userSeq, Long memoryId) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		List<Comment> comment = commentQueryRepository.findComment(memoryId);
		return convertNestedStructure(comment, user);
	}

	@Transactional
	public Comment saveComment(CommentRequestDto commentRequestDto) {

		// 기존에 메모리가 존재하는지 체크
		Memory memory = memoryRepository.findById(commentRequestDto.getMemoryId()).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
		});

		Comment parent = commentRequestDto.getParentId() != null ?
			commentRepository.findById(commentRequestDto.getParentId()).orElseThrow(() -> {
				throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
			}) : null;

		return commentRepository.save(
			Comment.createComment(commentRequestDto.getContent(), commentRequestDto.getUserId(),
				memory.getId(), parent,
				commentRequestDto.getMentionUserId()));
	}

	@Transactional
	public void updateComment(Long commentId, String content) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
		});
		comment.updateComment(content);
	}

	@Transactional
	public void deleteComment(Long commentId) {

		Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(
			() -> {
				throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
			});

		if (comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
			comment.changeDeletedStatus(DeleteStatus.Y);
		} else { // 삭제 가능한 조상 댓글을 구해서 삭제
			commentRepository.delete(getDeletableAncestorComment(comment));
		}
	}

	@Transactional
	public void saveCommentLike(Long userSeq, Long commentId) {

		Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
		});
		CommentLike commentLike = CommentLike.createCommentLike(userSeq, comment);
		commentLikeRepository.save(commentLike);
	}

	@Transactional
	public void deleteCommentLike(Long userSeq, Long commentId) {

		CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userSeq)
			.orElseThrow(() -> {
				throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT_LIKE);
			});
		commentLikeRepository.delete(commentLike);
	}

	private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
		Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
		if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
			// 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
			return getDeletableAncestorComment(parent);
		return comment; // 삭제해야하는 댓글 반환

	}

	private CommentResponseDto convertNestedStructure(List<Comment> comments, User user) {

		List<CommentDto> result = new ArrayList<>();
		Map<Long, CommentDto> map = new HashMap<>();

		int cnt = (int)comments.stream().filter(c -> c.getIsDeleted().equals(DeleteStatus.N)).count();

		comments.stream().forEach(c -> {

			CommentDto dto;

			if (c.getMetionUser() != null) {
				User mentionUser = userRepository.findById(c.getMetionUser()).orElseThrow(() -> {
					throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
				});

				dto = convertCommentToDto(c, user, mentionUser.getNickname());
			} else {
				dto = convertCommentToDto(c, user, null);
			}

			// 그 댓글을 map에 넣고
			map.put(dto.getId(), dto);
			// 만약 부모 댓글이 있다면?
			if (c.getParent() != null) {
				// dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
				if (map.getOrDefault(c.getParent().getId(), null) != null) {
					map.get(c.getParent().getId()).getChildren().add(dto);
				}

			} else {
				result.add(dto);
			}
		});

		return new CommentResponseDto(cnt, result);

	}

}
