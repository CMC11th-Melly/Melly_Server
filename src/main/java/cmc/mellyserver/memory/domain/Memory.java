package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.enums.OpenType;
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

    // 1, 1.5 , 2 , 2.5 .... 5
    private Long stars;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @ElementCollection
    @CollectionTable(
            name = "keywords_table",
            joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "keyword") // 컬럼명 지정 (예외)
    private List<String> keyword = new ArrayList<>();

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

    public void setMemoryImages(List<MemoryImage> memoryImages)
    {
        this.memoryImages = memoryImages;
        for (MemoryImage memoryImage : memoryImages) {
            memoryImage.setMemory(this);
        }
    }

    public void setKeyword(List<String> keywords)
    {
        // 값 타입처럼 아예 대체해버리기
        this.keyword = keywords;
    }

    @Builder
    public Memory(Long stars, GroupInfo groupInfo, String title, String content,OpenType openType) {
        this.stars = stars;
        this.groupInfo = groupInfo;
        this.title = title;
        this.content = content;
        this.openType = openType;
    }
}
