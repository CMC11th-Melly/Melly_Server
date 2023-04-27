package cmc.mellyserver.scrap.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapCancelRequest {
    private Double lat;
    private Double lng;
}
