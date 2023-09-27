package cmc.mellyserver.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.notification.constants.FCMConstants.PREFIX_FCMTOKEN;


@Component
@RequiredArgsConstructor
public class FCMTokenManageService {

    private final FCMTokenRepository fcmTokenRepository;

    public void saveToken(Long userId, String fcmToken) {
        fcmTokenRepository.saveToken(PREFIX_FCMTOKEN + userId, fcmToken);
    }


    public void deleteToken(Long userId) {
        fcmTokenRepository.deleteToken(PREFIX_FCMTOKEN + userId);
    }

    public boolean hasKey(Long userId) {
        return fcmTokenRepository.hasKey(PREFIX_FCMTOKEN + userId);
    }

    public String getToken(Long userId) {
        return fcmTokenRepository.getToken(PREFIX_FCMTOKEN + userId);
    }

}
