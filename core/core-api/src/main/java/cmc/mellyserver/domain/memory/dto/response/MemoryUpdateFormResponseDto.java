package cmc.mellyserver.domain.memory.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.domain.group.query.dto.GroupListForSaveMemoryResponseDto;
import lombok.Builder;
import lombok.Data;

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

    public static MemoryUpdateFormResponseDto of(Memory memory, List<UserGroup> userGroupLoginUserAssociated) {
        return new MemoryUpdateFormResponseDto(
            memory.getMemoryImages()
                .stream()
                .map(mi -> new MemoryImageDto(mi.getId(), mi.getImagePath()))
                .collect(Collectors.toList()),
            memory.getTitle(), memory.getContent(),
            userGroupLoginUserAssociated.stream()
                .map(ug -> new GroupListForSaveMemoryResponseDto(ug.getId(), ug.getName(), ug.getGroupType()))
                .collect(Collectors.toList()),
            memory.getStars(), null);
    }

}
