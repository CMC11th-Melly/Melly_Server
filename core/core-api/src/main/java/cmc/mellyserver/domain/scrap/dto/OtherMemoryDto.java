package cmc.mellyserver.domain.scrap.dto;

import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtherMemoryDto {

    private Long memoryId;

    private GroupType groupType;

    private List<MemoryImageDto> memoryImages;

    private String title;

    private List<String> keyword;

    private String createdDate;

}
