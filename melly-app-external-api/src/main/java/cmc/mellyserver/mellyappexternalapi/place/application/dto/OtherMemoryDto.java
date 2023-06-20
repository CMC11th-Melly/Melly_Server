package cmc.mellyserver.mellyappexternalapi.place.application.dto;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import java.util.List;
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
