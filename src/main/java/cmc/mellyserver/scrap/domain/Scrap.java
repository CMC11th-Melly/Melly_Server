package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private User user;  // 누가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place; // 어떤 장소를 스크랩?

    @Enumerated(EnumType.STRING)
    private ScrapType scrapType;

    // TODO : 장소와 관련된 사진을 추가할 수 있는지 확인하기

}
