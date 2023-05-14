package cmc.mellyserver.memory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MemoryUpdateFormResponseDto {

    // 메모리 상세 이미지, 이번에는 삭제를 위해서 id값을 넣어봄
    private List<MemoryImageDto> memoryImages;
    private String title;
    private String content;
    private List<GroupListForSaveMemoryResponseDto> userGroups;
    private Long star;
    private List<String> keywords;




}
