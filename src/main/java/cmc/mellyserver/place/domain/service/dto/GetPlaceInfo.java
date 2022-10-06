package cmc.mellyserver.place.domain.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
