package cmc.mellyserver.placeScrap.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapCancelRequest {
    private Double lat;
    private Double lng;
}
