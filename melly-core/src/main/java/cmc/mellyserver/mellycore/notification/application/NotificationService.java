package cmc.mellyserver.mellycore.notification.application;


import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import cmc.mellyserver.mellycore.notification.domain.repository.dto.NotificationResponse;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MongoTemplate mongoTemplate;

    private final MemoryRepository memoryRepository;

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationList(Long userId) {

        Query query = new Query(Criteria.where("user_id").is(userId));
        List<Notification> notificationList = mongoTemplate.findAll(Notification.class, "notification");
        return notificationList.stream().map(NotificationResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationOnOffResponseDto getNotificationStatus(Long userId) {
        User user = userRepository.getById(userId);
        return NotificationOnOffResponseDto.of(user.getEnableAppPush(), user.getEnableCommentPush(), user.getEnableCommentLikePush());
    }

    @Transactional
    public void changeAppPushStatus(Long userId, boolean status) {
        User user = userRepository.getById(userId);
        user.changeAppPushStatus(status);
    }

    @Transactional
    public void changeCommentLikePushStatus(Long userId, boolean status) {
        User user = userRepository.getById(userId);
        user.changeCommentLikePushStatus(status);
    }

    @Transactional
    public void changeCommentPushStatus(Long userId, boolean status) {
        User user = userRepository.getById(userId);
        user.changeCommenPushStatus(status);
    }


    @Transactional
    public Notification createNotification(String body, NotificationType notificationType, Long userId, Long memoryId) {

        User user = userRepository.getById(userId);
        Memory memory = memoryRepository.getById(memoryId);

        return mongoTemplate.insert(Notification.createNotification(
                body,
                userId,
                notificationType,
                false,
                user.getProfileImage(),
                user.getNickname(),
                memory.getId(),
                LocalDateTime.now()));
    }

    @Transactional
    public void checkNotification(Long notificationId) {

        Notification notification = mongoTemplate.findById(notificationId, Notification.class);
        notification.userCheckedNotification();
    }
}
