package cmc.mellyserver.mellycore.scrap.application.dto;

import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMemoryDto {

    private Long memoryId;
    private GroupType groupType;
    private List<MemoryImageDto> memoryImages;
    private String title;
    private List<String> keyword;
    private String createdDate;
}
