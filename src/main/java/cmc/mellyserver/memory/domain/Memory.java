package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


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


//    @Enumerated(EnumType.STRING)
//    private OpenType openType;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @OneToMany(mappedBy = "memory")
//    private List<MemoryImage> memoryImages = new ArrayList<>();
}
