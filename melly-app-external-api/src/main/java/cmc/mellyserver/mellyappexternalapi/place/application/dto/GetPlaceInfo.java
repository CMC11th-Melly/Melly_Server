package cmc.mellyserver.mellyappexternalapi.place.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPlaceInfo {

    private Boolean isScraped;

    private Long myMemoryCount;

    private Long otherMemoryCount;

}
