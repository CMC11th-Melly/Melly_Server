package cmc.mellyserver.domain.notification;


import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.notification.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.controller.notification.dto.response.NotificationResponse;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final MemoryRepository memoryRepository;

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationList(Long userId) {

        List<Notification> notificationList = notificationRepository.findAllByUserId(userId);
        return notificationList.stream().map(t -> new NotificationResponse(t.getId(), t.getNotificationType(), t.getContent()
                , t.getCreatedDateTime(), false)).collect(Collectors.toList());
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

        return notificationRepository.save(Notification.createNotification(
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

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_NOTIFICATION);
        });
        notification.userCheckedNotification();
    }
}
