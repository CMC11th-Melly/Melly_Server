package cmc.mellyserver.domain.memory.query.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.keyword.Keyword;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.place.Place;
import lombok.Builder;

public record MemoryResponseDto(

    Long placeId,
    String placeName,

    Long memoryId,
    String title,
    String content,
    List<MemoryImageDto> memoryImages,
    List<String> keywords,
    LocalDate visitedDate,
    long stars,

    Long groupId,
    GroupType groupType,
    String groupName,
    int groupIcon
) implements Serializable {
    @Builder
    public MemoryResponseDto {
    }

    public static MemoryResponseDto of(Place place, Memory memory, List<Keyword> keywords, UserGroup userGroup) {
        return MemoryResponseDto.builder()
            .placeId(place.getId())
            .placeName(place.getName())
            .memoryId(memory.getId())
            .title(memory.getTitle())
            .content(memory.getContent())
            .memoryImages(memory.getMemoryImages()
                .stream()
                .map(image -> new MemoryImageDto(image.getId(), image.getImagePath()))
                .toList())
            .keywords(keywords.stream().map(Keyword::getContent).toList())
            .visitedDate(memory.getVisitedDate())
            .stars(memory.getStars())
            .groupId(userGroup.getId())
            .groupType(userGroup.getGroupType())
            .groupName(userGroup.getName())
            .groupIcon(userGroup.getIcon())
            .build();
    }
}
