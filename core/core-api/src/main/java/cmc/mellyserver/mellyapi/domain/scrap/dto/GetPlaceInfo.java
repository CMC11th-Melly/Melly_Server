package cmc.mellyserver.mellyapi.domain.scrap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPlaceInfo {

    private Boolean isScraped;

    private Long myMemoryCount;

    private Long otherMemoryCount;

}
