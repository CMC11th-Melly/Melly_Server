package cmc.mellyserver.place.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import lombok.*;
import javax.persistence.*;


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

    @Column(name = "place_name")
    private String placeName;

    private String placeImage;

    private String placeCategory;

    private boolean isDeleted;

    @PrePersist
    public void init()
    {
        this.isDeleted = false;
    }

    public void delete()
    {
        this.isDeleted = true;
    }

    @Builder
    public Place(Position position,String placeImage,String placeCategory,String placeName) {
        this.position = position;
        this.placeName = placeName;
        this.placeImage = placeImage;
        this.placeCategory = placeCategory;
    }
}
