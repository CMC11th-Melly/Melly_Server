package cmc.mellyserver.mellyappexternalapi.memory.application.dto.response;

import cmc.mellyserver.mellydomain.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MemoryUpdateFormResponseDto {

    // 메모리 상세 이미지, 이번에는 삭제를 위해서 id값을 넣어봄
    private List<MemoryImageDto> memoryImages;
    private String title;
    private String content;
    private List<GroupListForSaveMemoryResponseDto> userGroups;
    private Long star;
    private List<String> keywords;

    @Builder
    public MemoryUpdateFormResponseDto(List<MemoryImageDto> memoryImages, String title, String content,
                                       List<GroupListForSaveMemoryResponseDto> userGroups, Long star, List<String> keywords) {
        this.memoryImages = memoryImages;
        this.title = title;
        this.content = content;
        this.userGroups = userGroups;
        this.star = star;
        this.keywords = keywords;
    }

}
