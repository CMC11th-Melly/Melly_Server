package cmc.mellyserver.place.placeScrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PlaceScrap extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_scrap_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq")
//    private User user;
      private Long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "place_id")
//    private Place place;
    private Long placeId;

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;


//    public PlaceScrap(ScrapType scrapType)
//    {
//        this.scrapType = scrapType;
//    }



//    public PlaceScrap(User user, Place place)
//    {
//        this.user = user;
//        this.place = place;
//    }

//    public void setUser(User user)
//    {
//        this.user= user;
//        user.getPlaceScraps().add(this);
//    }

//    public void setPlace(Place place)
//    {
//        this.place = place;
//        place.getScraps().add(this);
//    }

//    public static PlaceScrap createScrap(User user, Place place, ScrapType scrapType)
//    {
//        PlaceScrap scrap = new PlaceScrap(scrapType);
//        scrap.setPlace(place);
//        scrap.setUser(user);
//        return scrap;
//
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PlaceScrap scrap = (PlaceScrap) o;
//        return user.equals(scrap.user) && place.equals(scrap.place);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(user, place);
//    }
}
