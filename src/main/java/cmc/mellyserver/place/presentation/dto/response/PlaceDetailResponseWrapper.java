package cmc.mellyserver.place.presentation.dto.response;

import cmc.mellyserver.place.application.dto.GetPlaceInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceDetailResponseWrapper {
    private GetPlaceInfoDto placeDetail;
}
