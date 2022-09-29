package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
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
}
