package cmc.mellyserver.dbcore.memory.memory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_memory")
public class Memory extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "stars")
    private long stars;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "open_type")
    private OpenType openType;

    @Column(name = "visited_date")
    private LocalDate visitedDate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "memory_keyword", joinColumns = @JoinColumn(name = "memory_id"))
    private Set<Long> keywordIds = new HashSet<>();

    @Builder
    public Memory(Long stars, Long groupId, Long userId, Long placeId, String title, String content, OpenType openType,
        LocalDate visitedDate) {

        this.stars = stars;
        this.title = title;
        this.groupId = groupId;
        this.placeId = placeId;
        this.userId = userId;
        this.content = content;
        this.openType = openType;
        this.visitedDate = visitedDate;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public void update(String title, String content, Long groupId, OpenType openType, LocalDate visitedDate,
        long stars) {
        this.title = title;
        this.content = content;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.stars = stars;
    }

    public void setMemoryImages(List<MemoryImage> memoryImages) {
        this.memoryImages = memoryImages;
        memoryImages.forEach(image -> image.addMemory(this));
    }

    public void updateMemoryImages(List<Long> deleteImages, List<String> newImages) {
        memoryImages.removeIf((image) -> deleteImages.contains(image.getId()));
        List<MemoryImage> images = newImages.stream().map(MemoryImage::new).toList();
        memoryImages.addAll(images);
    }

    public void addKeywordIds(List<Long> ids) {
        if (Objects.nonNull(ids)) {
            keywordIds.addAll(ids);
        }
    }
}
