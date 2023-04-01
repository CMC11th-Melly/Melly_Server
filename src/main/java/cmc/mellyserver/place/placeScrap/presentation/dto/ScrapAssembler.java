package cmc.mellyserver.place.placeScrap.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.memory.memoryScrap.application.dto.ScrapedMemoryResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.domain.User;

import java.util.stream.Collectors;


public class ScrapAssembler {

    public static ScrapedPlaceResponseDto scrapedPlaceResponseDto(User user, Place place)
    {
        return new ScrapedPlaceResponseDto(place.getId(),place.getPosition(),place.getMemories().
                stream().
                filter(m -> m.getUser().getUserId().equals(user.getUserId())).count(),place.getMemories().stream().filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL)).count(),
                checkIsScraped(user,place),
                place.getPlaceCategory(),
                place.getPlaceName(),
                GroupType.ALL,
                place.getPlaceImage()
        );


    }

    public static ScrapedMemoryResponseDto scrapedMemoryResponseDto(Memory memory) {

        return new ScrapedMemoryResponseDto(memory.getPlace().getId(),memory.getPlace().getPlaceName(),memory.getId(),memory.getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),memory.getTitle(),
                memory.getContent(),memory.getGroupInfo().getGroupType(),memory.getGroupInfo().getGroupName(),memory.getStars(),memory.getKeyword(), memory.getVisitedDate());

    }

    private static boolean checkIsScraped(User user, Place place)
    {
        return user.getPlaceScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
    }
}
