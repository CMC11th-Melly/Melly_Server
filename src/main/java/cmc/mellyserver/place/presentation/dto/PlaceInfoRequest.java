package cmc.mellyserver.place.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfoRequest {

    @Schema(example = "34.0432423")
    private Double lat;

    @Schema(example = "127.0454544")
    private Double lng;

    @Schema(example = "용용선생")
    private String placeName;

    @Schema(example = "주류")
    private String placeCategory;

    @Schema(example = "진짜 인생 술집")
    private String title;

    @Schema(example = "동기들 데리고 꼭 다시 와볼만한 술집")
    private String content;

    @Schema(example = "좋아요")
    private String keyword;

    @Schema(example = "1")
    private Long groupId;

    @JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

    @Schema(example = "5")
    private Long star;

}
