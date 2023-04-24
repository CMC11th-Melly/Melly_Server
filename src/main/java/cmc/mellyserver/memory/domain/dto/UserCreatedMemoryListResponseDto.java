package cmc.mellyserver.memory.domain.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserCreatedMemoryListResponseDto.java
 *
 * @author jemlog
 */
@Data
public class UserCreatedMemoryListResponseDto {

    @Schema(example = "1")
    private Long placeId;
    @Schema(example = "용용선생")
    private String placeName;
    @Schema(example = "1")
    private Long memoryId;
    @Schema(example = "[melly.jpg,cmc.png]")
    private List<ImageDto> memoryImages;
    @Schema(example = "오랜만에 고향 친구랑!")
    private String title;
    @Schema(example = "다음에 친구들 데리고 다시 와야지!")
    private String content;
    @Schema(example = "FRIEND")
    private GroupType groupType;
    @Schema(example = "떡잎마을방범대")
    private String groupName;
    @Schema(example = "4.5")
    private Long stars;
    @Schema(example = "최고에요")
    private List<String> keyword;
    private boolean loginUserWrite;
    @Schema(example = "202210141830")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
    private LocalDateTime visitedDate;


    public UserCreatedMemoryListResponseDto(Long placeId, String placeName, Long memoryId, String title, String content, GroupType groupType, String groupName, Long stars, boolean loginUserWrite, LocalDateTime visitedDate) {
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
