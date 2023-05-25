package cmc.mellyserver.memory.application.dto.request;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.request.MemoryUpdateRequest;
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

    private java.util.List<String> keyword;

    private Long groupId;

    private OpenType openType;

    private LocalDateTime visitedDate;

    private Long star;

    private List<Long> deleteImageList;

    List<MultipartFile> images;

    @Builder
    public UpdateMemoryRequestDto(Long userSeq, Long memoryId, String title, String content, List<String> keyword, Long groupId, OpenType openType, LocalDateTime visitedDate, Long star, List<Long> deleteImageList, List<MultipartFile> images) {
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

    public static UpdateMemoryRequestDto of(Long userSeq, Long memoryId, MemoryUpdateRequest memoryUpdateRequest, List<MultipartFile> images)
    {
        return UpdateMemoryRequestDto.builder()
                .userSeq(userSeq)
                .memoryId(memoryId)
                .title(memoryUpdateRequest.getTitle())
                .content(memoryUpdateRequest.getContent())
                .keyword(memoryUpdateRequest.getKeyword())
                .groupId(memoryUpdateRequest.getGroupId())
                .openType(memoryUpdateRequest.getOpenType())
                .visitedDate(memoryUpdateRequest.getVisitedDate())
                .star(memoryUpdateRequest.getStar())
                .deleteImageList(memoryUpdateRequest.getDeleteImageList())
                .images(images)
                .build();

    }
}
