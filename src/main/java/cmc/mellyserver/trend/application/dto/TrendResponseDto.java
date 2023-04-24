package cmc.mellyserver.trend.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TrendResponseDto implements Serializable {
    private PlaceInfo placeInfo;
    private List<MemoryInfo> memoryInfo;

    public TrendResponseDto(Long placeId, String placeImage, String placeCategory, GroupType recommendType, Boolean isScraped, String placeName, List<Memory> memories, String groupName, User user)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,recommendType,isScraped,placeName);
        this.memoryInfo = memories.stream().map(m ->
                new MemoryInfo(placeId,placeName,m.getId(),
                        m.getMemoryImages().stream().
                                map(tm -> new ImageDto(tm.getId(),tm.getImagePath())).collect(Collectors.toList()),
                        m.getTitle(),
                        m.getContent(),
                        m.getGroupInfo().getGroupType(),
                        m.getGroupInfo().getGroupName(),
                        m.getStars(),
                        m.getKeyword(),
                        user.getMemories().stream().anyMatch((um -> um.getId().equals(m.getId()))),
                m.getVisitedDate()))
                .collect(Collectors.toList());

    }

    @Data
    static class PlaceInfo implements Serializable{
        private Long placeId;
        private String placeImage;
        private String placeCategory;
        private GroupType recommendType;
        private Boolean isScraped;
        private String placeName;

        public PlaceInfo(Long placeId, String placeImage, String placeCategory,GroupType recommendType,Boolean isScraped,String placeName)
        {
            this.placeId = placeId;
            this.placeImage = placeImage;
            this.placeCategory = placeCategory;
            this.recommendType= recommendType;
            this.isScraped = isScraped;
            this.placeName = placeName;
        }
    }

    @Data
    static class MemoryInfo implements Serializable{

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
        @Schema(example = "즐거워요, 그냥 그래요")
        private List<String> keyword;
        @Schema(description = "로그인 유저가 작성한 메모리인지 판별")
        private boolean loginUserWrite;
        @Schema(example = "202210142210")
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
        private LocalDateTime visitedDate;

        public MemoryInfo(Long placeId, String placeName, Long memoryId,List<ImageDto> memoryImages, String title, String content,GroupType groupType, String groupName,Long stars, List<String> keyword,boolean isLoginUserWrite, LocalDateTime visitedDate)
        {
            this.placeId = placeId;
            this.placeName = placeName;
            this.memoryId = memoryId;
            this.memoryImages = memoryImages;
            this.title = title;
            this.content = content;
            this.groupType = groupType;
            this.groupName = groupName;
            this.stars =stars;
            this.keyword = keyword;
            this.loginUserWrite = isLoginUserWrite;
            this.visitedDate = visitedDate;
        }
    }



}
