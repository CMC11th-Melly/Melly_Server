package cmc.mellyserver.memory.presentation.dto.request;

import cmc.mellyserver.common.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryUpdateRequest {

    private String title;

    private String content;

    private List<String> keyword;

    private Long groupId;

    private OpenType openType;

    private LocalDateTime visitedDate;

    private Long star;

    private List<Long> deleteImageList;

    @Builder
    public MemoryUpdateRequest(String title, String content, List<String> keyword, Long groupId, OpenType openType, LocalDateTime visitedDate, Long star, List<Long> deleteImageList) {
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.star = star;
        this.deleteImageList = deleteImageList;
    }
}
