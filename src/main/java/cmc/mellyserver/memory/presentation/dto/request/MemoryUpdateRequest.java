package cmc.mellyserver.memory.presentation.dto.request;

import cmc.mellyserver.common.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
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
}
