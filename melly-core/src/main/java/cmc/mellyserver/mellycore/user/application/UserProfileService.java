package cmc.mellyserver.mellycore.user.application;


import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.aws.AwsService;
import cmc.mellyserver.mellycore.common.aws.FileUploader;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellycore.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final AwsService awsService;

    private final FileUploader fileUploader;

    private final UserRepository userRepository;

    /**
     * 캐싱 적용 여부 : 가능
     */
    @Transactional(readOnly = true)
    public Integer checkImageStorageVolumeLoginUserUse(String username) {
        return awsService.calculateImageVolume("mellyimage", username).intValue();
    }

    /**
     * 캐싱 적용 여부 : 불가능
     * 이유 : 프로필 정보를 수정하기 위해 조회하는 데이터는 항상 최신성이 보장되야 한다. 따라서 캐시 적용 불가능하다.
     */
    @Transactional(readOnly = true)
    public ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(Long userSeq) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return new ProfileUpdateFormResponseDto(user.getProfileImage(), user.getNickname(), user.getGender(), user.getAgeGroup());
    }

    @Transactional(readOnly = true)
    public String findNicknameByUserIdentifier(Long userSeq) {

        User user = userRepository.findById(userSeq).orElseThrow(() -> {
            throw new UserNotFoundException();
        });

        return user.getNickname();
    }

    @Transactional
    public void updateLoginUserProfile(ProfileUpdateRequestDto profileUpdateRequestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(profileUpdateRequestDto.getUserSeq());
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(), profileUpdateRequestDto.getAgeGroup());

        if (profileUpdateRequestDto.isDeleteImage()) {
            user.chnageProfileImage(null);
        } else {
            user.chnageProfileImage(fileUploader.getMultipartFileName(profileUpdateRequestDto.getProfileImage()));
        }
    }
}
