package cmc.mellyserver.domain.scrap.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPlaceInfoDto {

    private String name;

    private Boolean isScraped;

    private String placeImage;

    private List<MyMemoryDto> myMemoryDtoList;

    private List<OtherMemoryDto> otherMemoryDtoList;

}
