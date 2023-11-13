package cmc.mellyserver.domain.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateMemoryRequestDto {

    Long userId;

    Position position;

    String title;

    String placeName;

    String placeCategory;

    String content;

    Long star;

    Long groupId;

    OpenType openType;

    List<String> keyword;

    LocalDate visitedDate;

    List<MultipartFile> multipartFiles;

    @Builder
    public CreateMemoryRequestDto(Long userId, Double lat, Double lng, String title, String placeName,
        String placeCategory, String content, Long star, Long groupId, OpenType openType, List<String> keyword,
        LocalDate visitedDate, List<MultipartFile> multipartFiles) {
        this.userId = userId;
        this.position = new Position(lat, lng);
        this.title = title;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
        this.content = content;
        this.star = star;
        this.groupId = groupId;
        this.openType = openType;
        this.keyword = keyword;
        this.visitedDate = visitedDate;
        this.multipartFiles = multipartFiles;
    }

    public Place toPlace() {
        return Place.builder()
            .position(position)
            .category(placeCategory)
            .name(placeName)
            .build();
    }

    public Memory toMemory() {
        return Memory.builder()
            .title(title)
            .userId(userId)
            .content(content)
            .groupId(groupId)
            .openType(openType)
            .stars(star)
            .visitedDate(visitedDate)
            .build();
    }

}
