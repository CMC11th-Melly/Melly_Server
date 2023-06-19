package cmc.mellyserver.mellyappexternalapi.place.application.dto;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
