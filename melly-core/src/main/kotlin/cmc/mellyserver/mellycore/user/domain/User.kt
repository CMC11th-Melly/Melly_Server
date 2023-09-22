package cmc.mellyserver.mellycore.user.domain

import cmc.mellyserver.mellycore.user.domain.enums.*
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(

    @Column(name = "social_id")
    val socialId: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    val provider: Provider,

    @Column(name = "email")
    val email: String?,

    @Column(name = "password")
    var password: String?,

    @Column(name = "nickname")
    var nickname: String,

    @Column(name = "profile_image")
    var profileImage: String?,

    @Embedded
    val recommend: Recommend?,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    val gender: Gender,

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group")
    val ageGroup: AgeGroup,

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    var roleType: RoleType,

    @Column(name = "enable_app_push")
    var enableAppPush: Boolean,

    @Column(name = "enable_comment_like_push")
    var enableCommentLikePush: Boolean,

    @Column(name = "enable_comment_push")
    var enableCommentPush: Boolean,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    var userStatus: UserStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "password_expired")
    var passwordExpired: PasswordExpired,

    @Column(name = "password_init_date")
    var pwInitDateTime: LocalDateTime,

    @Column(name = "last_login_time")
    var lastLoginDateTime: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {
}