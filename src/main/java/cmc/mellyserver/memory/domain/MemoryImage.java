package cmc.mellyserver.memory.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemoryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_image_id")
    private Long id;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "memory_id")
    private Memory memory;
}
