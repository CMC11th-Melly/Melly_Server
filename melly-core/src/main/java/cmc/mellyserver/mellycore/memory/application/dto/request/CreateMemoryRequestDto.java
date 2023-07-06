package cmc.mellyserver.mellycore.memory.application.dto.request;

import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateMemoryRequestDto {

    Long userSeq;
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
    LocalDateTime visitedDate;
    List<MultipartFile> multipartFiles;

    @Builder
    public CreateMemoryRequestDto(Long userSeq, Double lat, Double lng, String title, String placeName,
                                  String placeCategory, String content, Long star, Long groupId, OpenType openType, List<String> keyword,
                                  LocalDateTime visitedDate, List<MultipartFile> multipartFiles) {
        this.userSeq = userSeq;
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

    public Place toEntity() {
        return Place.builder().position(new Position(lat, lng)).placeCategory(placeCategory).placeName(placeName).build();
    }
}
