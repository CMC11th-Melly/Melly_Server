package cmc.mellyserver.place.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GetPlaceInfoDto {

    @Schema(example = "성수동",description = "검색한 장소의 이름입니다")
    private String name;

    private Boolean isScraped;

    private String placeImage;

    private List<MyMemoryDto> myMemoryDtoList;

    private List<OtherMemoryDto> otherMemoryDtoList;



}
