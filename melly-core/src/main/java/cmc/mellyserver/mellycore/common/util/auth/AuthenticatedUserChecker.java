package cmc.mellyserver.mellycore.common.util.auth;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserChecker {
    private final UserRepository userRepository;


    public User checkAuthenticatedUserExist(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_USER);
        });
    }
}
