package cmc.mellyserver.memory.presentation.dto.response;


import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetOtherMemoryForPlaceResponse {

    private Long placeId;
    private String placeName;
    private Long memoryId;
    private List<ImageDto> memoryImages;
    private String title;
    private String content;
    private GroupType groupType;
    private String groupName;
    private Long stars;
    private List<String> keyword;
    private boolean loginUserWrite;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
    private LocalDateTime visitedDate;

}
