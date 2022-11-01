package cmc.mellyserver.placeScrap.presentation.dto;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;

import java.util.stream.Collectors;


public class ScrapAssembler {

    public static ScrapedPlaceResponseDto scrapedPlaceResponseDto(Place place)
    {
        return new ScrapedPlaceResponseDto(place.getId(),place.getPosition(),
                true,
                place.getPlaceName(),
                place.getPlaceImage(),
                place.getPlaceCategory()
        );


    }

    public static ScrapedMemoryResponseDto scrapedMemoryResponseDto(Memory memory) {

        return new ScrapedMemoryResponseDto(memory.getPlace().getId(),memory.getPlace().getPlaceName(),memory.getId(),memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),memory.getTitle(),
                memory.getContent(),memory.getGroupInfo().getGroupType(),memory.getGroupInfo().getGroupName(),memory.getStars(),memory.getKeyword(), memory.getVisitedDate());

    }
}
