package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.util.DateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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

//    @Schema(example = "떡잎마을 방범대")
//    private String groupName;

    @JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

//    @Schema(example = "FRIEND")
//    private GroupType groupType;

    @Schema(example = "5")
    private Long star;

}
