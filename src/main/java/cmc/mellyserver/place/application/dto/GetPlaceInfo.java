package cmc.mellyserver.place.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GetPlaceInfo {

    private Boolean isScraped;

    private Long myMemoryCount;

    private Long otherMemoryCount;



}
