package cmc.mellyserver.user.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupMemory {
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
    @Schema(description = "로그인 유저가 작성한 메모리인지 판별")
    private boolean loginUserWrite;
    @Schema(example = "기뻐요, 그저 그래요")
    private List<String> keyword;
    @Schema(example = "202210142310")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
    private LocalDateTime visitedDate;


}
