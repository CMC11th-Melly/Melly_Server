package cmc.mellyserver.place.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Place extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Embedded
    @Column(name = "place_position")
    private Position position;

    @Column(name = "place_name")
    private String name;

    /*
    하나의 메모리에는 하나의 장소 설정 가능
    하나의 장소에는 여러 메모리들이 존재 가능
    나의 메모리, 나 이외의 전체 메모리 다 조회 가능
     */
    @OneToMany(mappedBy = "place")
    private List<Memory> memories = new ArrayList<>();
}
