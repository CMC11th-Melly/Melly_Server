package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;

    public static PlaceScrap createScrap(User user,Place place,ScrapType scrapType)
    {
        return new PlaceScrap(user,place,scrapType);
    }

    public PlaceScrap(User user, Place place, ScrapType scrapType) {
        this.user = user;
        this.place = place;
        this.scrapType = scrapType;
    }
}
