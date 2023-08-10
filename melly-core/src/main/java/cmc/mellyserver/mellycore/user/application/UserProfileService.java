package cmc.mellyserver.mellycore.user.application;


import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.infrastructure.storage.StorageService;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    private final StorageService fileUploader;


    /**
     * 캐싱 적용 여부 : 가능
     */
    @Cacheable(value = "image-volume:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public Integer checkImageStorageVolumeLoginUserUse(final Long userId) {

        User user = userRepository.getById(userId);
        return fileUploader.calculateImageVolume(user.getEmail()).intValue();
    }

    /**
     * 캐싱 적용 여부 : 불가능
     * 이유 : 프로필 정보를 수정하기 위해 조회하는 데이터는 항상 최신성이 보장되야 한다. 따라서 캐시 적용 불가능하다.
     */
    @Transactional(readOnly = true)
    public ProfileUpdateFormResponseDto getLoginUserProfileDataForUpdate(final Long userId) {

        User user = userRepository.getById(userId);
        return new ProfileUpdateFormResponseDto(user.getProfileImage(), user.getNickname(), user.getGender(), user.getAgeGroup());
    }

    @Cacheable(value = "profile:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public ProfileResponseDto getUserProfile(final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return ProfileResponseDto.of(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }

    @CacheEvict(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfile(final Long userId, ProfileUpdateRequestDto profileUpdateRequestDto) {

        User user = userRepository.getById(userId);
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(), profileUpdateRequestDto.getAgeGroup());
    }

    @CacheEvict(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfileImage(final Long userId, final MultipartFile profileImage) throws IOException {

        User user = userRepository.getById(userId);
        String userProfileImage = user.getProfileImage();
        removeExistprofileImage(userProfileImage);

        if (Objects.isNull(profileImage) || profileImage.isEmpty()) {
            user.changeProfileImage(null);
        } else {
            String newImageFile = fileUploader.saveFile(user.getId(), profileImage);
            user.changeProfileImage(newImageFile);
        }
    }

    private void removeExistprofileImage(final String userProfileImage) throws IOException {
        if (!Objects.isNull(userProfileImage)) {
            fileUploader.deleteFile(userProfileImage);
        }
    }

}
