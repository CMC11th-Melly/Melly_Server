package cmc.mellyserver.domain.notification.application;

import cmc.mellyserver.config.IntegrationTest;

public class NotificationServiceIntegrationTest extends IntegrationTest {

	// @DisplayName("로그인한 유저의 푸시 알림 관련 설정을 조회한다.")
	// @Test
	// void get_notification_setting() {
	// // given
	// User user = User.cmc.mellyserver.mellycore.builder()
	// .nickname("테스트 유저")
	// .enableAppPush(true)
	// .enableComment(true)
	// .enableCommentLike(false)
	// .build();
	//
	// User savedUser = userRepository.save(user);
	//
	// // when
	// NotificationOnOffResponseDto notificationOnOff =
	// notificationService.getNotificationOnOff(
	// savedUser.getId());
	//
	// // then
	// assertThat(notificationOnOff.isEnableAppPush()).isTrue();
	// assertThat(notificationOnOff.isEnableContent()).isTrue();
	// assertThat(notificationOnOff.isEnableContentLike()).isFalse();
	// }
	//
	// @DisplayName("알림을 생성한다.")
	// @Test
	// void create_notification() {
	// // given
	// User user = User.cmc.mellyserver.mellycore.builder()
	// .nickname("테스트 유저")
	// .enableAppPush(true)
	// .enableComment(true)
	// .enableCommentLike(false)
	// .build();
	//
	// User savedUser = userRepository.save(user);
	//
	// CreateMemoryRequestDto createMemoryRequestDto =
	// CreateMemoryRequestDto.cmc.mellyserver.mellycore.builder()
	// .id(user.getId()).title("테스트 제목")
	// .content("테스트 컨텐츠").placeName("테스트 장소")
	// .placeCategory("카페").lat(1.234).lng(1.234)
	// .build();
	//
	// Memory memory = memoryService.createMemory(createMemoryRequestDto);
	//
	// // when
	// Notification notification =
	// notificationService.createNotification(NotificationType.COMMENT, "테스트 알림",
	// savedUser.getId(), memory.getId());
	//
	// // then
	// assertThat(notification.isChecked()).isFalse();
	// assertThat(notification.getMessage()).isEqualTo("테스트 알림");
	// }
	//
	// @DisplayName("알림을 생성할 때 메모리가 DB에 존재하지 않으면 예외가 발생한다.")
	// @Test
	// void create_notification_not_exist_exception() {
	//
	// // given
	// User user = User.cmc.mellyserver.mellycore.builder()
	// .nickname("테스트 유저")
	// .enableAppPush(true)
	// .enableComment(true)
	// .enableCommentLike(false)
	// .build();
	//
	// User savedUser = userRepository.save(user);
	//
	// CreateMemoryRequestDto createMemoryRequestDto =
	// CreateMemoryRequestDto.cmc.mellyserver.mellycore.builder()
	// .id(user.getId()).title("테스트 제목")
	// .content("테스트 컨텐츠").placeName("테스트 장소")
	// .placeCategory("카페").lat(1.234).lng(1.234)
	// .build();
	//
	// memoryService.createMemory(createMemoryRequestDto);
	//
	// // when then
	// assertThatThrownBy(() -> {
	// notificationService.createNotification(NotificationType.COMMENT, "테스트 알림",
	// savedUser.getId(), -1L);
	// })
	// .isInstanceOf(GlobalBadRequestException.class)
	// .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());
	// }
	//
	// @DisplayName("사용자가 알림을 클릭하면 체크 상태가 True가 된다.")
	// @Test
	// void check_notification() {
	//
	// // given
	// Notification notification =
	// Notification.createNotification(NotificationType.COMMENT, "테스트 알림", false, 1L, 2L);
	// Notification savedNotification = notificationRepository.save(notification);
	// // when
	// notificationService.checkNotification(savedNotification.getId());
	//
	// // then
	// Notification findNotification =
	// notificationRepository.findById(savedNotification.getId()).get();
	// assertThat(findNotification.isChecked()).isTrue();
	// }

}
