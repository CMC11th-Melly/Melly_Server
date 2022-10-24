package cmc.mellyserver.placeScrap.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.domain.User;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


public class ScrapAssembler {

    public static ScrapedPlaceResponseDto scrapedPlaceResponseDto(Place place, User user, LocalDateTime createdDate)
    {
        return new ScrapedPlaceResponseDto(place.getId(),place.getPosition(),place.getMemories().
                stream().
                filter(m -> m.getUser().getUserId().equals(user.getUserId())).count(),place.getMemories().stream().filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL)).count(),
                true,
                place.getPlaceName(),
                GroupType.ALL,
                place.getPlaceImage(),
                place.getPlaceCategory(),
                createdDate
        );


    }

    public static ScrapedMemoryResponseDto scrapedMemoryResponseDto(Memory memory) {

        return new ScrapedMemoryResponseDto(memory.getId(),memory.getMemoryImages().stream().map(mi -> mi.getImagePath()).collect(Collectors.toList()),memory.getTitle(),
                memory.getContent(),memory.getGroupInfo().getGroupType(),memory.getGroupInfo().getGroupName(),memory.getStars(),memory.getKeyword(), memory.getVisitedDate());

    }
}
