package cmc.mellyserver.memory.presentation.dto.response;

import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class MemoryUpdateFormResponse {

    private List<MemoryImageDto> memoryImages;
    private String title;
    private String content;
    private List<GroupListForSaveMemoryResponseDto> userGroups;
    private Long star;
    private List<String> keywords;

    @Builder
    public MemoryUpdateFormResponse(List<MemoryImageDto> memoryImages, String title, String content, List<GroupListForSaveMemoryResponseDto> userGroups, Long star, List<String> keywords) {
        this.memoryImages = memoryImages;
        this.title = title;
        this.content = content;
        this.userGroups = userGroups;
        this.star = star;
        this.keywords = keywords;
    }
}
