package cmc.mellyserver.controller.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.memory.OpenType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryUpdateRequest {

    private String title;

    private String content;

    private List<String> keyword;

    private Long groupId;

    private OpenType openType;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate visitedDate;

    private Long star;

    private List<Long> deleteImageList;

    @Builder
    public MemoryUpdateRequest(String title, String content, List<String> keyword, Long groupId, OpenType openType,
        LocalDate visitedDate, Long star, List<Long> deleteImageList) {
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
