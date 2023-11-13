package cmc.mellyserver.controller.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.memory.OpenType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryCreateRequest {

    private Double lat;

    private Double lng;

    private String placeName;

    private String placeCategory;

    @NotNull
    @Length(max = 23, message = "제목은 23자 이하로 입력해주세요.")
    private String title;

    @NotNull
    @Length(max = 650, message = "본문은 650자 이하로 작성해주세요.") // ok
    private String content;

    private List<String> keyword;

    private Long groupId;

    private OpenType openType;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate visitedDate;

    private Long star;

    @Builder
    public MemoryCreateRequest(Double lat, Double lng, String placeName, String placeCategory, String title,
        String content, List<String> keyword, Long groupId, OpenType openType, LocalDate visitedDate, Long star) {
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.star = star;
    }

}
