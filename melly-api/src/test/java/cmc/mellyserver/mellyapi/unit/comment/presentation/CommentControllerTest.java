package cmc.mellyserver.mellyapi.unit.comment.presentation;

import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.CommentUpdateRequest;
import cmc.mellyserver.mellyapi.comment.presentation.dto.request.LikeRequest;
import cmc.mellyserver.mellyapi.common.annotation.WithUser;
import cmc.mellyserver.mellyapi.unit.ControllerTest;
import cmc.mellyserver.mellycore.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentRequest;
import static cmc.mellyserver.mellyapi.common.docs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerTest {

    @DisplayName("댓글에 좋아요를 달 수 있다.")
    @WithUser
    @Test
    void add_comment_like() throws Exception {

        CommentLike commentLike = CommentLike.createCommentLike(1L, null);
        given(commentService.saveCommentLike(anyLong(), anyLong())).willReturn(commentLike);

        // when
        ResultActions perform = mockMvc.perform(post("/api/comment/like")
                .content(objectMapper.writeValueAsString(new LikeRequest(1L, 2L)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(commentService, times(1))
                .saveCommentLike(anyLong(), anyLong());

        perform.andDo(document("add_comment_like",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));
    }

    @DisplayName("댓글에 단 좋아요를 취소할 수 있다.")
    @WithUser
    @Test
    void cancel_comment_like() throws Exception {

        doNothing().when(commentService).deleteCommentLike(anyLong(), anyLong());

        // when
        ResultActions perform = mockMvc.perform(delete("/api/comment/1/like")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(commentService, times(1))
                .deleteCommentLike(anyLong(), anyLong());

        perform.andDo(document("remove_comment_like",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));
    }

    @DisplayName("댓글을 생성할 수 있다.")
    @WithUser
    @Test
    void create_comment() throws Exception {

        CommentRequest commentRequest = CommentRequest.builder()
                .parentId(1L)
                .mentionUserId(2L)
                .memoryId(3L)
                .content("테스트 댓글")
                .build();
        Comment comment = Comment.createComment("테스트 댓글", 1L, 2L, null, 3L);
        given(commentService.saveComment(any(CommentRequestDto.class))).willReturn(comment);

        // when
        ResultActions perform = mockMvc.perform(post("/api/comment")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentRequest))
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(commentService, times(1))
                .saveComment(any(CommentRequestDto.class));

        perform.andDo(document("create_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));
    }

    @DisplayName("댓글을 수정할 수 있다.")
    @WithUser
    @Test
    void update_comment() throws Exception {

        // given
        CommentUpdateRequest commentUpdateRequest = new CommentUpdateRequest("수정된 내용");
        Comment comment = Comment.createComment("테스트 내용", 1L, 2L, null, 3L);

        given(commentService.updateComment(anyLong(), anyString())).willReturn(comment);

        // when
        ResultActions perform = mockMvc.perform(put("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentUpdateRequest))
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(commentService, times(1))
                .updateComment(anyLong(), anyString());

        perform.andDo(document("update_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));
    }

    @DisplayName("댓글을 삭제할 수 있다.")
    @WithUser
    @Test
    void remove_comment() throws Exception {

        doNothing().when(commentService).deleteComment(anyLong());

        // when
        ResultActions perform = mockMvc.perform(delete("/api/comment/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"));

        verify(commentService, times(1))
                .deleteComment(anyLong());

        perform.andDo(document("remove_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷")
                )));
    }

}
