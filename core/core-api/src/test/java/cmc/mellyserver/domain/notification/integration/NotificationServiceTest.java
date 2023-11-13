package cmc.mellyserver.domain.notification.integration;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.domain.notification.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import fixtures.MemoryFixtures;

public class NotificationServiceTest extends IntegrationTestSupport {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemoryRepository memoryRepository;

	@Autowired
	private NotificationService notificationService;

	@DisplayName("유저의 푸시 알림 관련 설정을 조회한다")
	@Test
	void 유저의_푸시알림_설정을_조회한다() {

		// given
		User 모카 = userRepository.save(모카());

		// when
		NotificationOnOffResponseDto notificationOnOff = notificationService.getNotificationStatus(모카.getId());

		// then
		assertThat(notificationOnOff.isEnableAppPush()).isTrue();
		assertThat(notificationOnOff.isEnableContent()).isTrue();
		assertThat(notificationOnOff.isEnableContentLike()).isTrue();
	}

	@DisplayName("알림을 생성한다")
	@Test
	void 알림을_생성한다() {
		// given
		User 모카 = userRepository.save(모카());
		Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(1L, 모카.getId(), null, "스타벅스 방문!", OpenType.ALL));

		// when
		Notification notification = notificationService.createNotification("메세지 본문", NotificationType.COMMENT_ENROLL,
			모카.getId(), 메모리.getId());

		// then
		assertThat(notification.isRead()).isFalse();
		assertThat(notification.getContent()).isEqualTo("메세지 본문");
	}

	@DisplayName("알림을 생성할 때 메모리가 DB에 존재하지 않으면 예외가 발생한다.")
	@Test
	void 알람_생성시_메모리가_존재하지_않으면_예외를_반환한다() {

		// given
		User 모카 = userRepository.save(모카());

		// when & then
		assertThatThrownBy(() -> {
			notificationService.createNotification("메세지 본문", NotificationType.COMMENT_ENROLL, 모카.getId(), -1L);
		})
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());
	}

	@DisplayName("사용자가 알림을 클릭하면 읽음처리된다")
	@Test
	void 사용자가_알림을_클릭하면_읽음처리된다() {

		// given
		Notification notification = Notification.builder().notificationType(NotificationType.COMMENT_ENROLL)
			.isRead(false).build();
		Notification savedNotification = notificationRepository.save(notification);
		// when
		notificationService.readNotification(savedNotification.getId());

		// then
		Notification findNotification = notificationRepository.findById(savedNotification.getId()).get();
		assertThat(findNotification.isRead()).isTrue();
	}

}
