package cmc.mellyserver.memory.application.dto.request;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.place.presentation.dto.request.MemoryCreateRequest;
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
    public CreateMemoryRequestDto(Long userSeq, Double lat, Double lng, String title, String placeName, String placeCategory, String content, Long star, Long groupId, OpenType openType, List<String> keyword, LocalDateTime visitedDate, List<MultipartFile> multipartFiles) {
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

    public static CreateMemoryRequestDto of(Long userSeq, List<MultipartFile> images, MemoryCreateRequest placeInfoRequest)
    {
        return CreateMemoryRequestDto.builder()
                .userSeq(userSeq)
                .lat(placeInfoRequest.getLat())
                .lng(placeInfoRequest.getLng())
                .title(placeInfoRequest.getTitle())
                .placeName(placeInfoRequest.getPlaceName())
                .placeName(placeInfoRequest.getPlaceCategory())
                .content(placeInfoRequest.getContent())
                .star(placeInfoRequest.getStar())
                .groupId(placeInfoRequest.getGroupId())
                .openType(placeInfoRequest.getOpenType())
                .keyword(placeInfoRequest.getKeyword())
                .visitedDate(placeInfoRequest.getVisitedDate())
                .multipartFiles(images)
                .build();
    }
}
