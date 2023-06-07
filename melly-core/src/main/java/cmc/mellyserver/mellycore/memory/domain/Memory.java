package cmc.mellyserver.mellycore.memory.domain;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Table(name = "tb_memory", indexes = {
        @Index(name = "idx__groupType", columnList = "groupType"),
        @Index(name = "idx__stars", columnList = "stars"),
        @Index(name = "id__openType", columnList = "openType")
})
public class Memory extends JpaBaseEntity {

    @Embedded
    GroupInfo groupInfo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long id;
    private Long stars;
    private Long userId;
    private Long placeId;
    private String title;
    @Lob
    private String content;
    @Enumerated(EnumType.STRING)
    private OpenType openType;

    private boolean isDelete;

    private boolean isReported = false;

    private LocalDateTime visitedDate;

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "tb_keywords_table",
            joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "keyword") // 컬럼명 지정 (예외)
    private List<String> keyword = new ArrayList<>();

    @Builder
    public Memory(Long stars, GroupInfo groupInfo, Long userId, String title, String content,
            OpenType openType,
            LocalDateTime visitedDate, boolean isDelete) {
        this.stars = stars;
        this.groupInfo = groupInfo;
        this.title = title;
        this.userId = userId;
        this.content = content;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.isDelete = isDelete;
    }

    @PrePersist
    public void init() {
        this.isDelete = false;
    }

    public void delete() {
        this.isDelete = true;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public void updateMemory(String title, String content, List<String> keyword, Long groupId,
            GroupType groupType,
            String groupName, OpenType openType, LocalDateTime visitedDate, Long star) {
        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.groupInfo = new GroupInfo(groupName, groupType, groupId);
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.stars = star;
    }

    public void setMemoryImages(List<MemoryImage> memoryImages) {
        this.memoryImages = memoryImages;
        for (MemoryImage memoryImage : memoryImages) {
            memoryImage.setMemory(this);
        }
    }

    public void updateMemoryImages(List<MemoryImage> memoryImages) {
        for (MemoryImage memoryImage : memoryImages) {
            this.getMemoryImages().add(memoryImage);
            memoryImage.setMemory(this);
        }
    }

    public void setKeyword(List<String> keywords) {
        this.keyword = keywords;
    }

}
