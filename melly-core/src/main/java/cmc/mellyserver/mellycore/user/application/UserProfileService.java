package cmc.mellyserver.mellycore.user.application;


import cmc.mellyserver.mellycore.infrastructure.storage.StorageService;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Objects;

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


    @Cacheable(value = "profile:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public ProfileResponseDto getUserProfile(final Long userId) {

        User user = userRepository.getById(userId);
        return ProfileResponseDto.of(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }

    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfile(final Long userId, final ProfileUpdateRequestDto profileUpdateRequestDto) {

        User user = userRepository.getById(userId);
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(), profileUpdateRequestDto.getAgeGroup());
    }


    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfileImage(final Long userId, final MultipartFile profileImage) throws IOException {

        User user = userRepository.getById(userId);

        removeExistprofileImage(user.getProfileImage());

        if (Objects.isNull(profileImage)) {
            user.changeProfileImage(null);
        } else {
            String newImageFile = fileUploader.saveFile(user.getId(), profileImage);
            user.changeProfileImage(newImageFile);
        }
    }

    private void removeExistprofileImage(final String userProfileImage) throws IOException {

        if (Objects.nonNull(userProfileImage)) {
            fileUploader.deleteFile(userProfileImage);
        }
    }

}
