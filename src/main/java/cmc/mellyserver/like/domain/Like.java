package cmc.mellyserver.like.domain;

import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memory_id")
    private Memory memory;

    @Enumerated(EnumType.STRING)
    private LikeType likeType;

    public void setMemory(Memory memory)
    {
        this.memory = memory;
        memory.getLikes().add(this);
    }

    public Like(LikeType likeType)
    {
        this.likeType = likeType;
    }

    public static Like createLike(Memory memory,LikeType likeType)
    {
        Like like = new Like(likeType);
        like.setMemory(memory);
        return like;
    }





}
