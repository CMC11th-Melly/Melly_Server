package cmc.mellyserver.mellyapi.controller.place.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceSimpleRequest {
    private Double lat;
    private Double lng;
}
