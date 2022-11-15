package cmc.mellyserver.report.memoryReport.domain;

import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryReport extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    private Memory memory;

    private String content;


    public MemoryReport(User user, Memory memory,String content)
    {
        this.user = user;
        user.getMemoryReports().add(this);
        this.memory = memory;
        memory.getMemoryReports().add(this);
        this.content = content;
    }

}
