package cmc.mellyserver.domain.scrap.dto;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.place.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MarkedPlaceResponseDto {

  private Position position;

  private GroupType groupType;

  private Long placeId;

  private Long memoryCount;

}
