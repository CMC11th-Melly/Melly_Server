package cmc.mellyserver.builder;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.user.UserRepository;
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
