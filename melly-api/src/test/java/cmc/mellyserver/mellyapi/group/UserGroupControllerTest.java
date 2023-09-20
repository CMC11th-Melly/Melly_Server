package cmc.mellyserver.mellyapi.group;

import cmc.mellyserver.mellyapi.ControllerTest;

public class UserGroupControllerTest extends ControllerTest {

//    @DisplayName("그룹의 식별자를 통해 특정 그룹의 정보를 조회할 수 있다.")
//    @WithUser
//    @Test
//    void getGroupInfo() throws Exception {
//
//        // given
//        UserGroup userGroup = UserGroupFactory.mockUserGroup();
//        given(groupService.findGroupById(anyLong())).willReturn(userGroup);
//
//        // when
//        ResultActions perform = mockMvc.perform(get("/api/group/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(groupService, times(1))
//                .findGroupById(anyLong());
//
//        perform.andDo(document("get-group-info",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
//                        fieldWithPath("data.groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
//                        fieldWithPath("data.groupIcon").type(JsonFieldType.NUMBER).description("그룹 아이콘"),
//                        fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
//                        fieldWithPath("data.users").type(JsonFieldType.NULL).description("유저 목록"),
//                        fieldWithPath("data.groupType").type(JsonFieldType.STRING).description("그룹 타입"),
//                        fieldWithPath("data.invitationLink").type(JsonFieldType.STRING).description("그룹 초대 링크")
//                )));
//
//    }
//
//    @DisplayName("그룹을 추가할 수 있다.")
//    @WithUser
//    @Test
//    void create_group() throws Exception {
//
//        // given
//        GroupCreateRequest groupCreateRequest = UserGroupFactory.mockGroupCreateRequest();
//        given(groupService.saveGroup(any(CreateGroupRequestDto.class)))
//                .willReturn(UserGroupFactory.mockUserGroup());
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/api/group")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(groupCreateRequest))
//                .accept(MediaType.ALL)
//                .with(csrf())
//        );
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(groupService, times(1))
//                .saveGroup(any(CreateGroupRequestDto.class));
//
//        perform.andDo(document("create-group",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("공통 데이터 포맷"),
//                        fieldWithPath("data.groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
//                        fieldWithPath("data.groupIcon").type(JsonFieldType.NUMBER).description("그룹 아이콘"),
//                        fieldWithPath("data.groupName").type(JsonFieldType.STRING).description("그룹 이름"),
//                        fieldWithPath("data.users").type(JsonFieldType.NULL).description("그룹에 속한 유저 목록"),
//                        fieldWithPath("data.groupType").type(JsonFieldType.STRING).description("그룹 종류"),
//                        fieldWithPath("data.invitationLink").type(JsonFieldType.STRING).description("초대링크")
//                )));
//
//    }
//
//    @DisplayName("그룹을 수정할 수 있다.")
//    @WithUser
//    @Test
//    void update_group() throws Exception {
//
//        // given
//        GroupUpdateRequest groupUpdateRequest = UserGroupFactory.mockGroupUpdateRequest();
//        Mockito.doNothing().when(groupService).updateGroup(anyLong(), any(UpdateGroupRequestDto.class));
//
//        // when
//        ResultActions perform = mockMvc.perform(put("/api/group/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(groupUpdateRequest))
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(groupService, times(1))
//                .updateGroup(anyLong(), any(UpdateGroupRequestDto.class));
//
//        perform.andDo(document("update-group",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("공통 응답 메세지")
//                )));
//    }
//
//    @DisplayName("그룹을 삭제할 수 있다.")
//    @WithUser
//    @Test
//    void remove_group() throws Exception {
//
//        // given
//        Mockito.doNothing().when(groupService).removeGroup(anyLong(), anyLong());
//
//        // when
//        ResultActions perform = mockMvc.perform(delete("/api/group/1")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .with(csrf())
//                .accept(MediaType.ALL));
//
//        // then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.message").value("성공"));
//
//        verify(groupService, times(1))
//                .removeGroup(anyLong(), anyLong());
//
//        perform.andDo(document("delete-group",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                responseFields(
//                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
//                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
//                        fieldWithPath("data").type(JsonFieldType.NULL).description("성공 메세지")
//                )));
//
//    }
}
