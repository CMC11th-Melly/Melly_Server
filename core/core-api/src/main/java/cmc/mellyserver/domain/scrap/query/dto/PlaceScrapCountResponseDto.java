package cmc.mellyserver.domain.scrap.query.dto;

import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
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
