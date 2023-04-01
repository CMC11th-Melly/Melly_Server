package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class PlaceAssembler {

    public static List<PlaceListReponseDto> placeListReponseDto(List<Place> places,GroupType groupType,Long userSeq)
    {
       return places.stream().map(p -> new PlaceListReponseDto(
                new Position(p.getPosition().getLat(),p.getPosition().getLng()),
                groupType,
                p.getId(),
                p.getMemories().stream().filter(pm -> {

                    // 만약 메모리가 전체 공개라면
                    if(groupType.equals(GroupType.ALL))
                    {
                        // 해당 메모리의 작성자가 로그인 유저와 같은지 체크
                        return pm.getUser().getUserSeq().equals(userSeq);
                    }
                    else{   // 아니면 메모리의 그룹 정보가 내가 원하는 정보와 같고, 유저가 작성자인지 체크
                      return pm.getGroupInfo().getGroupType().equals(groupType) & pm.getUser().getUserSeq().equals(userSeq);
                    }
                }).count()
                )).collect(Collectors.toList());
    }
   // & m.getOpenType().equals(OpenType.ALL)
    public static PlaceResponseDto placeResponseDto(Place place, Boolean isScraped, HashMap<String,Long> memoryCounts)
    {
         return new PlaceResponseDto(place.getId(),place.getPosition(),memoryCounts.get("belongToUSer"),memoryCounts.get("NotBelongToUSer"),
                 isScraped,place.getPlaceCategory(),place.getPlaceName(),GroupType.ALL,place.getPlaceImage());
    }


}


//        return new PlaceResponseDto(place.getId(),place.getPosition(),place.getMemories().
//                stream().
//                filter(m -> m.getUser().getUserSeq().equals(userSeq)).count(),
//                place.getMemories().stream().filter(m -> (!m.getUser().getUserId().equals(user.getUserId()))  & user.getMemoryBlocks().stream().noneMatch(mb -> mb.getMemory().getId().equals(m.getId()))).count(),
//                isScraped,
//                place.getPlaceCategory(),
//                place.getPlaceName(),
//                GroupType.ALL,
//                place.getPlaceImage()
//                );