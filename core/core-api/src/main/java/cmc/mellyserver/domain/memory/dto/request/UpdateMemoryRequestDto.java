package cmc.mellyserver.domain.memory.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.dbcore.memory.memory.OpenType;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateMemoryRequestDto {

    private Long id;

    private Long memoryId;

    private String title;

    private String content;

    private List<MultipartFile> images;

    private List<String> keyword;

    private Long groupId;

    private OpenType openType;

    private LocalDate visitedDate;

    private Long star;

    private List<Long> deleteImageList;

    @Builder
    public UpdateMemoryRequestDto(Long id, Long memoryId, String title, String content, List<String> keyword,
        Long groupId, OpenType openType, LocalDate visitedDate, Long star, List<Long> deleteImageList,
        List<MultipartFile> images) {
        this.id = id;
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
