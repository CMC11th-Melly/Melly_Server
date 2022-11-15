package cmc.mellyserver.memoryScrap.presentation.dto;

import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;


@Data
@AllArgsConstructor
public class MemoryScrapResponseWrapper {
    private Slice<ScrapedMemoryResponseDto> scrapedMemory;
}
