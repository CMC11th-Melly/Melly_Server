package cmc.mellyserver.dbcore.memory;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_keyword")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "memoryId")
    private Memory memory;

    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
