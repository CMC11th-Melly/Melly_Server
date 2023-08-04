package cmc.mellyserver.mellycore.scrap.domain.repository.dto;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceScrapCountResponseDto implements Serializable {

    private ScrapType scrapType;
    private Long scrapCount;
}
