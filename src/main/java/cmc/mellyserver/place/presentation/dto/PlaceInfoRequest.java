package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfoRequest {

    @Schema(example = "34.0432423")
    private Double lat;
    @Schema(example = "127.0454544")
    private Double lng;
    @Schema(example = "진짜 인생 술집")
    private String title;
    @Schema(example = "동기들 데리고 꼭 다시 와볼만한 술집")
    private String content;
    @Schema(example = "주류")
    private String keyword;
    @Schema(example = "1")
    private Long groupId;
    @Schema(example = "5")
    private int star;
    @Schema(example = "FRIEND")
    private GroupType groupType;
    @Schema(example = "ALL")
    private OpenType  openType;
}
