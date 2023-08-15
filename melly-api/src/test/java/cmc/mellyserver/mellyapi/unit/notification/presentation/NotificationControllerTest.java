package cmc.mellyserver.mellyapi.unit.notification.presentation;

import cmc.mellyserver.mellyapi.unit.ControllerTest;

public class NotificationControllerTest extends ControllerTest {

//    @DisplayName("알림 설정 세팅값을 가지고 올 수 있다.")
//    @WithUser
//    @Test
//    void get_notification_setting() throws Exception {
//
//        // given
//        NotificationOnOffResponseDto notificationOnOffResponseDto = NotificationOnOffResponseDto.builder()
//                .enableAppPush(true)
//                .enableContent(true)
//                .enableContentLike(true)
//                .build();
//
//        given(notificationService.getNotificationOnOff(anyLong()))
//                .willReturn(notificationOnOffResponseDto);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/notification/setting")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("get-notification-setting", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
//                        fieldWithPath("data.enableAppPush").type(JsonFieldType.BOOLEAN).description("앱 푸시 허용 여부"),
//                        fieldWithPath("data.enableContent").type(JsonFieldType.BOOLEAN).description("댓글이 달리면 알림 보내기 허용 여부"),
//                        fieldWithPath("data.enableContentLike").type(JsonFieldType.BOOLEAN)
//                                .description("댓글에 좋아요 달리면 알림 보내기 허용 여부"))));
//
//        // then
//        verify(notificationService, times(1))
//                .getNotificationOnOff(anyLong());
//    }
//
//    @DisplayName("내가 받은 알림 리스트를 받아올 수 있다.")
//    @WithUser
//    @Test
//    void get_notification_list() throws Exception {
//
//        // given
//        Notification notification = Notification.createNotification(NotificationType.COMMENT, "테스트 알림", false, 1L, 2L);
//        given(notificationService.getNotificationList(anyLong()))
//                .willReturn(List.of(notification));
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/notification")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.ALL));
//
//        // then
//        // perform.andExpect(status().isOk())
//        // 	.andExpect(jsonPath("$.code").value(200))
//        // 	.andExpect(jsonPath("$.message").value("성공"))
//        // 	.andDo(document("get-notification-setting", responseFields(
//        // 		fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//        // 		fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//        // 		fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
//        // 		fieldWithPath("data.enableAppPush").type(JsonFieldType.BOOLEAN).description("앱 푸시 허용 여부"),
//        // 		fieldWithPath("data.enableContent").type(JsonFieldType.BOOLEAN).description("댓글이 달리면 알림 보내기 허용 여부"),
//        // 		fieldWithPath("data.enableContentLike").type(JsonFieldType.BOOLEAN)
//        // 			.description("댓글에 좋아요 달리면 알림 보내기 허용 여부"))));
//
//        // then
//        verify(notificationService, times(1))
//                .getNotificationList(anyLong());
//    }
//
//    @DisplayName("알림을 클릭한 경우, 읽을 처리 할 수 있다.")
//    @WithUser
//    @Test
//    void check_notification_status_read() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).checkNotification(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/notification/check")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(new NotificationCheckRequest(1L)))
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).checkNotification(anyLong());
//    }
//
//    @DisplayName("푸시 알림 활성화 하기")
//    @WithUser
//    @Test
//    void active_app_push() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setAppPushOn(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/notification/setting")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setAppPushOn(anyLong());
//    }
//
//    @DisplayName("푸시 알림 비활성화 하기")
//    @WithUser
//    @Test
//    void inactive_app_push() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setAppPushOff(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(delete("/api/notification/setting")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setAppPushOff(anyLong());
//    }
//
//    @DisplayName("컨텐츠가 생성됐을때 알림 활성화")
//    @WithUser
//    @Test
//    void active_content() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setPushCommentOn(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/notification/setting/comment")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setPushCommentOn(anyLong());
//    }
//
//    @DisplayName("컨텐츠 생성 알림 비활성화")
//    @WithUser
//    @Test
//    void inactive_content() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setPushCommentOff(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(delete("/api/notification/setting/comment")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setPushCommentOff(anyLong());
//    }
//
//    @DisplayName("좋아요 알림 활성화")
//    @WithUser
//    @Test
//    void active_like() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setPushCommentLikeOn(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/notification/setting/comment/like")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setPushCommentLikeOn(anyLong());
//    }
//
//    @DisplayName("좋아요 알림 비활성화")
//    @WithUser
//    @Test
//    void inactive_like() throws Exception {
//
//        // given
//        Mockito.doNothing().when(notificationService).setPushCommentLikeOff(anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(delete("/api/notification/setting/comment/like")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"))
//                .andDo(document("check_read", responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 데이터 포맷"))));
//
//        // then
//        verify(notificationService, times(1)).setPushCommentLikeOff(anyLong());
//    }

}
