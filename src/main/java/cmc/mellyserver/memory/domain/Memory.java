package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
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

    @OneToMany(mappedBy = "memory",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    public void setPlaceForMemory(Place place)
    {
        this.place = place;
    }
    public void setUser(User user)
    {
        this.user=  user;
        user.getMemories().add(this);
    }

    public void setMemoryImages(MemoryImage memoryImages)
    {
        this.memoryImages.add(memoryImages);
        memoryImages.setMemory(this);
    }

    @Builder
    public Memory(int stars, String keyword, GroupInfo groupInfo, OpenType openType, String title, String content) {
        this.stars = stars;
        this.keyword = keyword;
        this.groupInfo = groupInfo;
        this.openType = openType;
        this.title = title;
        this.content = content;
    }
}
