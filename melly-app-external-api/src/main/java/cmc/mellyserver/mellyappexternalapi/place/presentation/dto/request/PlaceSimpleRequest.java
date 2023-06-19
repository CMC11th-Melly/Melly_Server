package cmc.mellyserver.mellyappexternalapi.place.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceSimpleRequest {
	private Double lat;
	private Double lng;
}
