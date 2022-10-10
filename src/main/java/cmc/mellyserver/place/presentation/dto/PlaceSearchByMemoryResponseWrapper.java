package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceSearchByMemoryResponseWrapper {
    private PlaceResponseDto placeInfo;
}
