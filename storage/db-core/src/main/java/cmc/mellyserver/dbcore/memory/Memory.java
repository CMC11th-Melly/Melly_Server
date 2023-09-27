package cmc.mellyserver.dbcore.memory;


import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.memory.enums.OpenType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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
    private Long id; // 메모리 ID

    @Column(name = "stars")
    private Long stars; // 별점 (정렬 기준)

    @Column(name = "user_id")
    private Long userId; // 메모리 작성자

    @Column(name = "place_id")
    private Long placeId; // 연관 장소 ID

    /*
    그룹 이름과 그룹 타입은 실시간으로 변경될 필요성이 있다.
    따라서 메모리 엔티티 내부에 groupName과 groupType을 배치하기 보다는 join을 통해 가져오기로 결정
    그룹과 엔티티가 도메인 관계 상으로는 독립적이기 때문에 연관관계로 묶는건 배제함
     */
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "title")
    private String title; // 메모리 제목

    @Column(name = "content")
    @Lob
    private String content; // 메모리 컨텐츠

    @Enumerated(EnumType.STRING)
    @Column(name = "open_type")
    private OpenType openType; // 공개 타입

    @Column(name = "is_deleted")
    private Boolean is_deleted; // 삭제 여부

    private LocalDate visitedDate; // 장소 방문 날짜

    @OneToMany(mappedBy = "memory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemoryImage> memoryImages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords = new ArrayList<>(); // 키워드 목록

    @Builder
    public Memory(Long stars, Long groupId, Long userId, Long placeId, String title, String content, OpenType openType, LocalDate visitedDate, List<String> keyword) {

        this.stars = stars;
        this.title = title;
        this.groupId = groupId;
        this.placeId = placeId;
        this.userId = userId;
        this.content = content;
        this.openType = openType;
        this.visitedDate = visitedDate;
    }


    @PrePersist
    public void init() {
        this.is_deleted = Boolean.FALSE;
    }

    public void delete() {
        this.is_deleted = Boolean.TRUE;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public void updateMemory(String title, String content, List<Long> keywordList, Long groupId, OpenType openType, LocalDate visitedDate, Long star, List<Long> deleteImageList, List<String> multipartNames) {

        this.title = title;
        this.content = content;
        this.groupId = groupId;
        this.openType = openType;
        this.visitedDate = visitedDate;
        this.stars = star;
        updateMemoryImage(deleteImageList, multipartNames);
        updateKeyword(keywordList);
    }

    public void setMemoryImages(List<MemoryImage> memoryImages) {

        this.memoryImages = memoryImages;
        for (MemoryImage memoryImage : memoryImages) {
            memoryImage.setMemory(this);
        }
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
        for (Keyword keyword : keywords) {
            keyword.setMemory(this);
        }
    }

    private void updateMemoryImage(List<Long> deleteImageList, List<String> multipartFileNames) {

        if (!deleteImageList.isEmpty()) {
            for (Long deleteId : deleteImageList) {
                this.memoryImages.removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }

        if (Objects.nonNull(multipartFileNames) || !multipartFileNames.isEmpty()) {
            setMemoryImages(multipartFileNames.stream().map(MemoryImage::new).collect(Collectors.toList()));
        }
    }

    private void updateKeyword(List<Long> deleteKeywordList) {

        if (!Objects.isNull(deleteKeywordList)) {
            for (Long deleteId : deleteKeywordList) {
                this.keywords.removeIf(memoryImage -> memoryImage.getId().equals(deleteId));
            }
        }
    }


}
