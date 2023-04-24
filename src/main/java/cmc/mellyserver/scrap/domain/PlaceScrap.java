package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.common.enums.ScrapType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaceScrap extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_scrap_id")
    private Long id;

    private Long userId;

    private Long placeId;

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;

    public static PlaceScrap createScrap(Long userSeq,Long placeId,ScrapType scrapType)
    {
        return new PlaceScrap(userSeq,placeId,scrapType);
    }

    public PlaceScrap(Long userId, Long placeId, ScrapType scrapType) {
        this.userId = userId;
        this.placeId = placeId;
        this.scrapType = scrapType;
    }
}
