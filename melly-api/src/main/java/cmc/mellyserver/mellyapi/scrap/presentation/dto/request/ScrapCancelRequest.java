package cmc.mellyserver.mellyapi.scrap.presentation.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapCancelRequest {

	private Double lat;

	private Double lng;
}
