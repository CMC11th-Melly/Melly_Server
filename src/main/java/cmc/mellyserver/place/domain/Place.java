package cmc.mellyserver.place.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Place extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Embedded
    @Column(name = "place_position")
    private Position position;

//    @Column
//    private Point location;

    @Column(name = "place_name")
    private String placeName;

    private Boolean isScraped = false;
    private String placeImage;

    private String placeCategory;

    public void setScraped(boolean scrap)
    {this.isScraped = scrap;}

    /*
    하나의 메모리에는 하나의 장소 설정 가능
    하나의 장소에는 여러 메모리들이 존재 가능
    나의 메모리, 나 이외의 전체 메모리 다 조회 가능
     */
    @OneToMany(mappedBy = "place",fetch = FetchType.LAZY)
    private List<Memory> memories = new ArrayList<>();

    @OneToMany(mappedBy = "place",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PlaceScrap> scraps = new ArrayList<>();
    @Builder
    public Place(Position position,String placeImage,String placeCategory,String placeName) {
        this.position = position;
        this.placeName = placeName;
        this.placeImage = placeImage;
        this.placeCategory = placeCategory;
    }

    public void removeScrap(User user)
    {
        PlaceScrap scrap = new PlaceScrap(user, this);
        this.getScraps().remove(scrap);
    }

    public void addScrap(User user)
    {
        PlaceScrap scrap = new PlaceScrap(user, this);
        this.getScraps().add(scrap);
    }


}
