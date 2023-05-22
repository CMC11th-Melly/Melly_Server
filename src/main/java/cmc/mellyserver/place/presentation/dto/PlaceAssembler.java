package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.application.dto.MarkedPlaceReponseDto;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.presentation.dto.response.MarkedPlaceReponse;
import cmc.mellyserver.place.presentation.dto.response.PlaceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PlaceAssembler {

    public static List<MarkedPlaceReponse> markedPlaceReponse(List<MarkedPlaceReponseDto> markedPlaceReponseDtos)
    {
        return markedPlaceReponseDtos.stream().map(each -> MarkedPlaceReponse.builder().placeId(each.getPlaceId()).position(each.getPosition()).groupType(each.getGroupType()).memoryCount(each.getMemoryCount()).build()).collect(Collectors.toList());
    }
    public static PlaceResponseDto placeResponseDto(Place place, Boolean isScraped, HashMap<String,Long> memoryCounts)
    {
        return new PlaceResponseDto(place.getId(),place.getPosition(),memoryCounts.get("belongToUSer"),memoryCounts.get("NotBelongToUSer"),
                isScraped,place.getPlaceCategory(),place.getPlaceName(),GroupType.ALL,place.getPlaceImage());
    }

    public static PlaceResponse placeResponse(PlaceResponseDto placeResponseDto)
    {
        return PlaceResponse.builder()
                .placeId(placeResponseDto.getPlaceId())
                .position(placeResponseDto.getPosition())
                .myMemoryCount(placeResponseDto.getMyMemoryCount())
                .otherMemoryCount(placeResponseDto.getOtherMemoryCount())
                .isScraped(placeResponseDto.getIsScraped())
                .placeCategory(placeResponseDto.getPlaceCategory())
                .placeName(placeResponseDto.getPlaceName())
                .recommendType(placeResponseDto.getRecommendType())
                .placeImage(placeResponseDto.getPlaceImage())
                .build();
    }

    public static List<MarkedPlaceReponseDto> markedPlaceResponseDto(List<Place> placeUserMemoryExist) {
        return placeUserMemoryExist.stream().map(each -> new MarkedPlaceReponseDto(each.getPosition(),null,null,null)).collect(Collectors.toList());
    }
}
