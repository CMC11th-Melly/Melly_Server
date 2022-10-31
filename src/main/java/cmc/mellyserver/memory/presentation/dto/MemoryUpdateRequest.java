package cmc.mellyserver.memory.presentation.dto;

import cmc.mellyserver.memory.domain.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryUpdateRequest {

    @Schema(example = "진짜 인생 술집")
    private String title;

    @Schema(example = "동기들 데리고 꼭 다시 와볼만한 술집")
    private String content;

    @Schema(example = "[좋아요, 그저그래요]")
    private List<String> keyword;

    @Schema(example = "1")
    private Long groupId;

    @Schema(example = "GROUP")
    private OpenType openType;

    @JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

    @Schema(example = "5")
    private Long star;

    @Schema(description = "삭제 해야 하는 이미지들 id 값 입니다")
    private List<Long> deleteImageList;
}
