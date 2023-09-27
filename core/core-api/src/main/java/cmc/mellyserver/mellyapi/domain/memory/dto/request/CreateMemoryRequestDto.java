package cmc.mellyserver.mellyapi.domain.memory.dto.request;


import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.enums.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMemoryRequestDto {

    Long userId;
    Double lat;
    Double lng;
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
        this.lat = lat;
        this.lng = lng;
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
                .position(new Position(lat, lng))
                .placeCategory(placeCategory)
                .placeName(placeName).build();
    }

    public Memory toMemory() {
        return Memory.builder()
                .title(title)
                .userId(userId)
                .keyword(keyword)
                .content(content)
                .groupId(groupId)
                .openType(openType)
                .stars(star)
                .visitedDate(visitedDate)
                .build();
    }

}
