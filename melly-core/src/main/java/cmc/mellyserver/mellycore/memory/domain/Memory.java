package cmc.mellyserver.mellycore.memory.domain;

import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_memory")
public class Memory extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long id;

    @Column(name = "stars")
    private Long stars;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "open_type")
    private OpenType openType;

    @Column(name = "is_deleted")
    private DeleteStatus is_deleted;


    private LocalDateTime visitedDate;

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "tb_keywords_table",
            joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "keyword")
    private List<String> keyword = new ArrayList<>();

    @Builder
    public Memory(Long stars, Long groupId, Long userId, Long placeId, String title, String content, OpenType openType, LocalDateTime visitedDate, List<String> keyword) {

        this.stars = stars;
        this.title = title;
        this.groupId = groupId;
        this.placeId = placeId;
        this.userId = userId;
        this.content = content;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.keyword = keyword;
    }


    @PrePersist
    public void init() {
        this.is_deleted = DeleteStatus.N;
    }

    public void delete() {
        this.is_deleted = DeleteStatus.Y;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public void updateMemory(String title, String content, List<String> keyword, Long groupId, OpenType openType, LocalDateTime visitedDate, Long star, List<Long> deleteImageList, List<String> multipartNames) {

        this.title = title;
        this.content = content;
        this.keyword = keyword;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.stars = star;
        updateMemoryImage(deleteImageList, multipartNames);
    }

    public void setMemoryImages(List<MemoryImage> memoryImages) {

        this.memoryImages = memoryImages;
        for (MemoryImage memoryImage : memoryImages) {
            memoryImage.setMemory(this);
        }
    }

    private void updateMemoryImage(List<Long> deleteImageList, List<String> multipartFileNames) {

        if (!Objects.isNull(deleteImageList)) {
            for (Long deleteId : deleteImageList) {
                this.memoryImages.removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }

        if (!Objects.isNull(multipartFileNames)) {
            setMemoryImages(multipartFileNames.stream().map(m -> new MemoryImage(m)).collect(Collectors.toList()));
        }
    }

    public void setKeyword(List<String> keywords) {
        this.keyword = keywords;
    }

}
