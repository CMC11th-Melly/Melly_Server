package cmc.mellyserver.mellycore.notification.application;


import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final MemoryRepository memoryRepository;

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<Notification> getNotificationList(Long userId) {
        return null;
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
    public Notification createNotification(String title, String body, NotificationType notificationType, Long userId, Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
        return notificationRepository.save(Notification.createNotification(title, body, notificationType, false, memory.getId(), userId));
    }

    @Transactional
    public void checkNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_NOTIFICATION);
        });
        notification.userCheckedNotification();
    }
}
