package cmc.mellyserver.scrap.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.domain.Scrap;
import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


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
}
