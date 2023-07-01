package cmc.mellyserver.mellyappexternalapi.common.auth;

import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserChecker {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User checkAuthenticatedUserExist(Long userSeq) {
        return userRepository.findById(userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
    }
}
