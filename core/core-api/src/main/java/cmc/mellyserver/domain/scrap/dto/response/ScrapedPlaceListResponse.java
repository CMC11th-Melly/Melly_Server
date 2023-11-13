package cmc.mellyserver.domain.scrap.dto.response;

import java.util.List;

import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapedPlaceListResponse {

  private List<ScrapedPlaceResponseDto> contents;

  private Boolean next;

  public static ScrapedPlaceListResponse from(List<ScrapedPlaceResponseDto> contents, Boolean next) {
	return new ScrapedPlaceListResponse(contents, next);
  }

}
