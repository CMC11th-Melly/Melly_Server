package cmc.mellyserver.user.domain;

import cmc.mellyserver.group.domain.Group;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String socialId;

    private String nickname;

    private String profileImage;

    private LocalDateTime birthday;

    private boolean gender;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

   // @Embedded
   // private VisitedPlace visitedPlace;

   // @ManyToOne
  //  @JoinColumn(name = "group_id")
  //  private Group group;
}
