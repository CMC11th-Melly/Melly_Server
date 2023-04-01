package cmc.mellyserver.memory.memoryScrap.presentation.dto;

import cmc.mellyserver.memory.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;


@Data
@AllArgsConstructor
public class MemoryScrapResponseWrapper {
    private Slice<ScrapedMemoryResponseDto> scrapedMemory;
}
