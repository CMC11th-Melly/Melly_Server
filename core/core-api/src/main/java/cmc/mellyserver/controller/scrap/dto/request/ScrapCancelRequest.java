package cmc.mellyserver.controller.scrap.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapCancelRequest {

	private Double lat;

	private Double lng;

}
