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

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_place_scrap")
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

//	private Long userId;
//
//	private Long placeId;

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;

//    @Builder
//    public PlaceScrap(Long userId, Long placeId, ScrapType scrapType) {
//        this.userId = userId;
//        this.placeId = placeId;
//        this.scrapType = scrapType;
//    }

    @Builder
    public PlaceScrap(User user, Place place, ScrapType scrapType) {
        this.user = user;
        this.place = place;
        this.scrapType = scrapType;
    }

    public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType) {
        return new PlaceScrap(user, place, scrapType);
    }
//	public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType) {
//		return new PlaceScrap(user.getUserSeq(), place.getId(), scrapType);
//	}
}
