package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PlaceAssembler {

    public static List<PlaceListReponseDto> placeListReponseDto(List<Place> places,GroupType groupType,String userId)
    {
       return places.stream().map(p -> new PlaceListReponseDto(
                new Position(p.getPosition().getLat(),p.getPosition().getLng()),
                groupType,
                p.getId(),
                p.getMemories().stream().filter(pm -> {

                    if(groupType.equals(GroupType.ALL))
                    {
                        return pm.getUser().getUserId().equals(userId);
                    }
                    else{
                      return pm.getGroupInfo().getGroupType().equals(groupType) & pm.getUser().getUserId().equals(userId);
                    }
                }).count()
                )).collect(Collectors.toList());
    }

    public static List<PlaceResponseDto> placeResponseDto(List<Place> places, User user)
    {
        return places.stream().map(p -> new PlaceResponseDto(p.getId(),p.getPosition(),
                p.getMemories().
                        stream().
                        filter(m -> m.getUser().getUserId().equals(user.getUserId())).count(),
                p.getMemories().stream().filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL)).count(),
                p.getIsScraped(),
                p.getPlaceCategory(),
                p.getPlaceName(),
                GroupType.ALL,
                p.getPlaceImage()
        )).collect(Collectors.toList());
    }


}
