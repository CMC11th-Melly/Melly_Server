package cmc.mellyserver.mellycore.scrap.domain;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_place_scrap")
public class PlaceScrap extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Enumerated(EnumType.STRING)
    @Column(name = "scrap_type", nullable = false)
    private ScrapType scrapType;

    @Builder
    public PlaceScrap(User user, Place place, ScrapType scrapType) {
        this.user = user;
        this.place = place;
        this.scrapType = scrapType;
    }

    public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType) {
        return new PlaceScrap(user, place, scrapType);
    }
}
