package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetMemoryForPlaceResponse {

    private Long memoryId;
    private List<String> memoryImages;
    private String title;
    private String content;
    private GroupType groupType;
    private String groupName;
    private Long stars;
    private List<String> keywords;
    private String createdDate;

}
