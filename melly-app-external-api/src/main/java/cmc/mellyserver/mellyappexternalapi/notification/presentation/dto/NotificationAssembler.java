package cmc.mellyserver.mellyappexternalapi.notification.presentation.dto;

import java.util.List;

import cmc.mellyserver.mellyappexternalapi.notification.presentation.dto.response.NotificationResponse;
import cmc.mellyserver.mellydomain.notification.domain.Notification;

public class NotificationAssembler {

	public static List<NotificationResponse> notificationResponses(List<Notification> notifications) {
		return null;
		//        return notifications.stream().map(noti -> new NotificationResponse(noti.getId(),noti.getTitle(),noti.getMessage(),noti.getCreatedDate(),noti.isChecked(),new NotificationResponse.NotificationMemoryDto(noti.getMemory().getPlace().getId(),noti.getMemory().getPlace().getPlaceName(),noti.getMemory().getId(),
		//                noti.getMemory().getMemoryImages().stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()),noti.getMemory().getTitle(),
		//                noti.getMemory().getContent(),noti.getMemory().getGroupInfo().getGroupType(),noti.getMemory().getGroupInfo().getGroupName(),noti.getMemory().getStars(),noti.getMemory().getKeyword(),noti.getMemory().getId().equals(noti.getMemory().getId()),noti.getMemory().getVisitedDate()))).collect(Collectors.toList());
	}

	private NotificationAssembler() {

	}
}