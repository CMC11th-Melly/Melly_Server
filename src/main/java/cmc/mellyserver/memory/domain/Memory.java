package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Memory extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long id;

    // 1,2,3,4,5
    private int stars;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    private String keyword;

    @Embedded
    GroupInfo groupInfo;

    @Enumerated(EnumType.STRING)
    private OpenType openType;

    private String title;
    // TODO : 차후에 길이 지정
    private String content;

    @OneToMany(mappedBy = "memory",fetch = FetchType.LAZY)
    private List<MemoryImage> memoryImages;




}
