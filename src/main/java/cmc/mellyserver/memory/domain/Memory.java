package cmc.mellyserver.memory.domain;

import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memoryScrap.domain.MemoryScrap;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Lob
    private String content;

    private LocalDateTime visitedDate;

    @OneToMany(mappedBy = "memory",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "memory",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<MemoryScrap> scraps = new ArrayList<>();

    public void setPlaceForMemory(Place place)
    {
        this.place = place;
    }
    public void setUser(User user)
    {
        this.user=  user;
        user.getMemories().add(this);
    }

    public void updateMemory(String title, String content, List<String> keyword, Long groupId, GroupType groupType, String groupName, OpenType openType, LocalDateTime visitedDate, Long star)
    {
          this.title = title;
          this.content = content;
          // 키워드는 아예 대체 해버리기
          this.keyword = keyword;
          this.groupInfo = new GroupInfo(groupName,groupType,groupId);
          this.openType = openType;
          this.visitedDate = visitedDate;
          this.stars = star;

    }

    public void setMemoryImages(List<MemoryImage> memoryImages)
    {
        this.memoryImages = memoryImages;
        for (MemoryImage memoryImage : memoryImages) {
            memoryImage.setMemory(this);
        }
    }

    public void updateMemoryImages(List<MemoryImage> memoryImages)
    {
        for (MemoryImage memoryImage : memoryImages) {
            this.getMemoryImages().add(memoryImage);
            memoryImage.setMemory(this);
        }
    }

    public void setKeyword(List<String> keywords)
    {
        // 값 타입처럼 아예 대체해버리기
        this.keyword = keywords;
    }

    @Builder
    public Memory(Long stars, GroupInfo groupInfo, String title, String content,OpenType openType,LocalDateTime visitedDate) {
        this.stars = stars;
        this.groupInfo = groupInfo;
        this.title = title;
        this.content = content;
        this.openType = openType;
        this.visitedDate = visitedDate;
    }

    public LocalDate getLocalDate()
    {
        return  this.getCreatedDate().toLocalDate();
    }
}
