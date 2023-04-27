package cmc.mellyserver.group.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MyGroupMemoryResponseDto.java
 *
 * @author jemlog
 */
@Data
public class MyGroupMemoryResponseDto {

    private Long placeId;

    private String placeName;

    private Long memoryId;

    private List<ImageDto> memoryImages;

    private String title;

    private String content;

    private GroupType groupType;

    private String groupName;

    private Long stars;

    private boolean loginUserWrite;

    private List<String> keyword;

    private LocalDateTime visitedDate;

    public MyGroupMemoryResponseDto(Long placeId, String placeName, Long memoryId, String title, String content, GroupType groupType, String groupName, Long stars, boolean loginUserWrite, LocalDateTime visitedDate) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.memoryId = memoryId;
        this.title = title;
        this.content = content;
        this.groupType = groupType;
        this.groupName = groupName;
        this.stars = stars;
        this.loginUserWrite = loginUserWrite;
        this.visitedDate = visitedDate;
    }
}
