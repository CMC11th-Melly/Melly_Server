package cmc.mellyserver.mellycore.infrastructure.message.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static cmc.mellyserver.mellycore.common.constants.FCMConstants.PREFIX_FCMTOKEN;


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

    public boolean hasKey(String email) {
        return fcmTokenRepository.hasKey(email);
    }

    public String getToken(String email) {
        return fcmTokenRepository.getToken(email);
    }

}
