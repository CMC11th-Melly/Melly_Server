package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OtherMemoryDto {
    private Long memoryId;
    private GroupType groupType;
    private List<MemoryImageDto> memoryImages;
    private String title;
    private String keyword;
    private String createdDate;
}
