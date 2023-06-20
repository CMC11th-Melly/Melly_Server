package cmc.mellyserver.mellyappexternalapi.unit.memory.presentation;

import cmc.mellyserver.mellyappexternalapi.common.annotation.WithUser;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellyappexternalapi.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.mellyappexternalapi.unit.ControllerTest;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.Memory;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.MemoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemoryControllerTest extends ControllerTest {

    @DisplayName("메모리를 추가할때 필요한 내가 속해있는 그룹 정보를 조회할 수 있다.")
    @WithUser
    @Test
    void get_group_list_for_add_memory() throws Exception {

        // given
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto1 = GroupListForSaveMemoryResponseDto.builder()
                .groupId(1L)
                .groupType(GroupType.FRIEND)
                .groupName("친구들")
                .build();
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto2 = GroupListForSaveMemoryResponseDto.builder()
                .groupId(2L)
                .groupType(GroupType.COMPANY)
                .groupName("회사 동료")
                .build();
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto3 = GroupListForSaveMemoryResponseDto.builder()
                .groupId(3L)
                .groupType(GroupType.COUPLE)
                .groupName("연인")
                .build();
        List<GroupListForSaveMemoryResponseDto> list = List.of(groupListForSaveMemoryResponseDto1,
                groupListForSaveMemoryResponseDto2, groupListForSaveMemoryResponseDto3);

        given(memoryService.findGroupListLoginUserParticipate(anyLong()))
                .willReturn(list);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/group")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andDo(document("get-group-list-for-add-memory", responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER)
                                        .description("그룹 ID"),
                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING)
                                        .description("그룹 타입"),
                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING)
                                        .description("그룹 이름")
                        )
                ));

        verify(memoryService, times(1))
                .findGroupListLoginUserParticipate(anyLong());
    }

    @DisplayName("특정 장소에 로그인한 사용자가 저장한 메모리 리스트를 조회한다.")
    @WithUser
    @Test
    void get_memories_login_user_write_in_place() throws Exception {

        // given
        MemoryResponseDto memoryResponseDto1 = MemoryResponseDto.builder()
                .memoryId(1L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto2 = MemoryResponseDto.builder()
                .memoryId(2L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto3 = MemoryResponseDto.builder()
                .memoryId(3L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        SliceImpl<MemoryResponseDto> slice = new SliceImpl<>(
                List.of(memoryResponseDto1, memoryResponseDto2, memoryResponseDto3),
                PageRequest.of(0, 10), true);

        given(memoryService.findLoginUserWriteMemoryBelongToPlace(any(Pageable.class), anyLong(),
                anyLong(),
                any(GroupType.class)))
                .willReturn(slice);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/user/place/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("groupType", "FRIEND")
                .param("pageable", String.valueOf(PageRequest.of(0, 10)))
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findLoginUserWriteMemoryBelongToPlace(any(Pageable.class), anyLong(), anyLong(),
                        any(GroupType.class));
    }

    @DisplayName("로그인한 사용자 이외의 유저가 저장한 메모리 리스트를 조회한다.")
    @WithUser
    @Test
    void get_memories_other_user_write_in_place() throws Exception {

        // given
        MemoryResponseDto memoryResponseDto1 = MemoryResponseDto.builder()
                .memoryId(1L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto2 = MemoryResponseDto.builder()
                .memoryId(2L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto3 = MemoryResponseDto.builder()
                .memoryId(3L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        SliceImpl<MemoryResponseDto> slice = new SliceImpl<>(
                List.of(memoryResponseDto1, memoryResponseDto2, memoryResponseDto3),
                PageRequest.of(0, 10), true);

        given(memoryService.findOtherUserWriteMemoryBelongToPlace(any(Pageable.class), anyLong(),
                anyLong(),
                any(GroupType.class)))
                .willReturn(slice);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/other/place/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("groupType", "FRIEND")
                .param("pageable", String.valueOf(PageRequest.of(0, 10)))
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findOtherUserWriteMemoryBelongToPlace(any(Pageable.class), anyLong(), anyLong(),
                        any(GroupType.class));
    }

    @DisplayName("내 그룹의 사람들이 이 장소에 그룹 공개나 전체 공개로 작성한 메모리를 조회한다.")
    @WithUser
    @Test
    void get_memories_my_group_user_write_in_place() throws Exception {

        // given
        MemoryResponseDto memoryResponseDto1 = MemoryResponseDto.builder()
                .memoryId(1L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto2 = MemoryResponseDto.builder()
                .memoryId(2L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        MemoryResponseDto memoryResponseDto3 = MemoryResponseDto.builder()
                .memoryId(3L)
                .title("다시 가고 싶어요")
                .content("다음에 또 갈께요")
                .build();
        SliceImpl<MemoryResponseDto> slice = new SliceImpl<>(
                List.of(memoryResponseDto1, memoryResponseDto2, memoryResponseDto3),
                PageRequest.of(0, 10), true);

        given(memoryService.findMyGroupMemberWriteMemoryBelongToPlace(any(Pageable.class),
                anyLong(), anyLong(),
                any(GroupType.class)))
                .willReturn(slice);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/group/place/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("groupType", "FRIEND")
                .param("pageable", String.valueOf(PageRequest.of(0, 10)))
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findMyGroupMemberWriteMemoryBelongToPlace(any(Pageable.class), anyLong(),
                        anyLong(), any(GroupType.class));
    }

    @DisplayName("메모리를 추가합니다.")
    @WithUser
    @Test
    void saveMemory() throws Exception {

        // given

        MemoryCreateRequest memoryCreateRequest = MemoryCreateRequest.builder()
                .title("메모리 제목")
                .content("메모리 컨텐츠입니다. 20자 이상 들어가는지 테스트 중입니다")
                .build();

        MockMultipartFile memoryData = new MockMultipartFile("memoryData", "other-file-name.data",
                "application/json",
                objectMapper.writeValueAsString(memoryCreateRequest).getBytes());

        given(memoryService.createMemory(any(CreateMemoryRequestDto.class))).willReturn(
                any(Memory.class));

        // when
        ResultActions perform = mockMvc.perform(multipart(HttpMethod.POST, "/api/memory")
                .file("images", "testImage1".getBytes())
                .file("images2", "testImage1".getBytes())
                .file(memoryData)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .createMemory(any(CreateMemoryRequestDto.class));
    }

    @DisplayName("메모리를 수정합니다.")
    @WithUser
    @Test
    void update_memory() throws Exception {

        // given
        MockMultipartFile mockFile1 = new MockMultipartFile("content", "filename",
                "multipart/form-data", "test".getBytes());
        MockMultipartFile mockFile2 = new MockMultipartFile("content", "filename",
                "multipart/form-data", "test".getBytes());

        MemoryUpdateRequest memoryUpdateRequest = MemoryUpdateRequest.builder().groupId(1L)
                .title("제목").build();
        MockMultipartFile memoryData = new MockMultipartFile("memoryData", "other-file-name.data",
                "application/json",
                objectMapper.writeValueAsString(memoryUpdateRequest).getBytes());

        Mockito.doNothing().when(memoryService).updateMemory(any(UpdateMemoryRequestDto.class));

        // when
        ResultActions perform = mockMvc.perform(multipart(HttpMethod.PUT, "/api/memory/1")
                .file(mockFile1)
                .file(mockFile2)
                .file(memoryData)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .updateMemory(any(UpdateMemoryRequestDto.class));
    }

    @DisplayName("메모리 수정을 위한 업데이트 폼을 조회한다.")
    @WithUser
    @Test
    void get_update_form() throws Exception {

        // given
        MemoryUpdateFormResponseDto memoryUpdateFormResponseDto = MemoryUpdateFormResponseDto.builder()
                .title("메모리 제목")
                .content("메모리 컨텐츠")
                .build();
        given(memoryService.findMemoryUpdateFormData(anyLong(), anyLong())).willReturn(
                memoryUpdateFormResponseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/1/update")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findMemoryUpdateFormData(anyLong(), anyLong());
    }

    @DisplayName("메모리를 삭제한다.")
    @WithUser
    @Test
    void remove_memory() throws Exception {

        // given
        Mockito.doNothing().when(memoryService).removeMemory(anyLong(), anyLong());

        // when
        ResultActions perform = mockMvc.perform(delete("/api/memory/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .with(csrf())
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));
        verify(memoryService, times(1)).removeMemory(anyLong(), anyLong());
    }

    @DisplayName("특정 메모리의 제목에 포함된 장소를 조회할 수 있다.")
    @WithUser
    @Test
    void get_place_contains_memory_name() throws Exception {

        // given
        FindPlaceInfoByMemoryNameResponseDto findPlaceInfoByMemoryNameResponseDto1 = FindPlaceInfoByMemoryNameResponseDto.builder()
                .placeId(1L)
                .memoryName("테스트")
                .build();
        FindPlaceInfoByMemoryNameResponseDto findPlaceInfoByMemoryNameResponseDto2 = FindPlaceInfoByMemoryNameResponseDto.builder()
                .placeId(2L)
                .memoryName("테스트")
                .build();
        FindPlaceInfoByMemoryNameResponseDto findPlaceInfoByMemoryNameResponseDto3 = FindPlaceInfoByMemoryNameResponseDto.builder()
                .placeId(3L)
                .memoryName("테스트")
                .build();
        List<FindPlaceInfoByMemoryNameResponseDto> list = List.of(
                findPlaceInfoByMemoryNameResponseDto1,
                findPlaceInfoByMemoryNameResponseDto2, findPlaceInfoByMemoryNameResponseDto3);

        given(memoryService.findPlaceInfoByMemoryName(anyLong(), anyString()))
                .willReturn(list);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/search")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("memoryName", "test")
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findPlaceInfoByMemoryName(anyLong(), anyString());
    }

    @DisplayName("메모리 식별자로 메모리를 찾는다.")
    @WithUser
    @Test
    void get_memory_by_memoryId() throws Exception {

        // given
        MemoryResponseDto memoryResponseDto = MemoryResponseDto.builder()
                .title("메모리 제목")
                .content("메모리 테스트 입니다.")
                .build();
        given(memoryService.findMemoryByMemoryId(anyLong(), anyLong()))
                .willReturn(memoryResponseDto);

        // when
        ResultActions perform = mockMvc.perform(get("/api/memory/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL));

        // then
        //        perform.andExpect(status().isOk())
        //                .andExpect(jsonPath("$.code").value(200))
        //                .andExpect(jsonPath("$.message").value("성공"))
        //                .andDo(document("get-group-list-for-add-memory", responseFields(
        //                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답코드"),
        //                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답메세지"),
        //                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("공통 데이터 포맷"),
        //                                fieldWithPath("data.[].groupId").type(JsonFieldType.NUMBER).description("그룹 ID"),
        //                                fieldWithPath("data.[].groupType").type(JsonFieldType.STRING).description("그룹 타입"),
        //                                fieldWithPath("data.[].groupName").type(JsonFieldType.STRING).description("그룹 이름")
        //                        )
        //                ));

        verify(memoryService, times(1))
                .findMemoryByMemoryId(anyLong(), anyLong());
    }
}
