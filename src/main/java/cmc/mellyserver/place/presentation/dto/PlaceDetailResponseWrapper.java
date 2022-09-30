package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.place.domain.service.dto.GetPlaceInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceDetailResponseWrapper {
    private GetPlaceInfoDto placeDetail;
}
