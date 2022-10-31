package cmc.mellyserver.memory.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    public void setMemory(Memory memory)
    {
        this.memory = memory;
    }

    public MemoryImage(String imagePath)
    {
        this.imagePath = imagePath;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryImage that = (MemoryImage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public MemoryImage(Long id)
    {
        this.id = id;
    }
}
