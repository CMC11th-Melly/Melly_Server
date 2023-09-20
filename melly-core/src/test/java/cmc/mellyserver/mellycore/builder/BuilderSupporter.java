package cmc.mellyserver.mellycore.builder;

import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Getter
@Component
@RequiredArgsConstructor
public class BuilderSupporter {

    private final UserRepository userRepository;
    private final MemoryRepository memoryRepository;
    private final NotificationRepository notificationRepository;
    private final PlaceScrapRepository placeScrapRepository;
    private final GroupAndUserRepository groupAndUserRepository;
    private final GroupRepository groupRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PlaceRepository placeRepository;
}
