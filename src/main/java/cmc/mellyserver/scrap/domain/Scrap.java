package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Scrap extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;

    public Scrap(ScrapType scrapType)
    {
        this.scrapType = scrapType;
    }

    public Scrap(User user, Place place)
    {
        this.user = user;
        this.place = place;
    }

    public void setUser(User user)
    {
        this.user= user;
        user.getScraps().add(this);
    }

    public void setPlace(Place place)
    {
        this.place = place;
        place.getScraps().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scrap scrap = (Scrap) o;
        return user.equals(scrap.user) && place.equals(scrap.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, place);
    }
}
