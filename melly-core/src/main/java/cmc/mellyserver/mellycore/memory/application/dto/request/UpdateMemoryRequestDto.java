package cmc.mellyserver.mellycore.memory.application.dto.request;

import cmc.mellyserver.mellycommon.enums.OpenType;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateMemoryRequestDto {

    Long userSeq;

    Long memoryId;

    String title;

    String content;
    List<MultipartFile> images;
    private java.util.List<String> keyword;
    private Long groupId;
    private OpenType openType;
    private LocalDateTime visitedDate;
    private Long star;
    private List<Long> deleteImageList;

    @Builder
    public UpdateMemoryRequestDto(Long userSeq, Long memoryId, String title, String content, List<String> keyword,
                                  Long groupId, OpenType openType, LocalDateTime visitedDate, Long star, List<Long> deleteImageList,
                                  List<MultipartFile> images) {
        this.userSeq = userSeq;
        this.memoryId = memoryId;
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.star = star;
        this.deleteImageList = deleteImageList;
        this.images = images;
    }

}
