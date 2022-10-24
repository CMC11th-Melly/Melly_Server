package cmc.mellyserver.memoryScrap.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemoryScrap extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    private Memory memory;

    public MemoryScrap(User user, Memory memory)
    {
        this.user = user;
        this.memory = memory;
    }

    public void setUser(User user)
    {
        this.user= user;
        user.getMemoryScraps().add(this);
    }

    public void setMemory(Memory memory)
    {
        this.memory = memory;
        memory.getScraps().add(this);
    }

    public static MemoryScrap createScrap(User user, Memory memory)
    {
        MemoryScrap scrap = new MemoryScrap();
        scrap.setMemory(memory);
        scrap.setUser(user);
        return scrap;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryScrap that = (MemoryScrap) o;
        return user.equals(that.user) && memory.equals(that.memory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, memory);
    }
}