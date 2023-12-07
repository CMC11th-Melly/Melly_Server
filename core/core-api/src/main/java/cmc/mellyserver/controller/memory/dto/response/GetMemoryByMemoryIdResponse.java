package cmc.mellyserver.controller.memory.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.memory.query.dto.MemoryImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMemoryByMemoryIdResponse {

    private Long placeId;

    private String placeName;

    private Long memoryId;

    private List<MemoryImageDto> memoryImages;

    private String title;

    private String content;

    private GroupType groupType;

    private String groupName;

    private Long stars;

    private List<String> keyword;

    private boolean loginUserWrite;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

}
