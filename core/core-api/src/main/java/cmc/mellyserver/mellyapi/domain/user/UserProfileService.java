package cmc.mellyserver.mellyapi.domain.user;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.file.FileDto;
import cmc.mellyserver.file.StorageService;
import cmc.mellyserver.mellyapi.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellyapi.domain.user.dto.response.ProfileUpdateRequestDto;
import cmc.mellyserver.mellyapi.support.exception.BusinessException;
import cmc.mellyserver.mellyapi.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    private final StorageService fileUploader;


    @Cacheable(value = "image-volume:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public Integer checkImageStorageVolumeLoginUserUse(final Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return fileUploader.calculateImageVolume(user.getEmail()).intValue();
    }


    @Cacheable(value = "profile:user-id", key = "#userId")
    @Transactional(readOnly = true)
    public ProfileResponseDto getUserProfile(final Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return ProfileResponseDto.of(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }


    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfile(final Long userId, final ProfileUpdateRequestDto profileUpdateRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(), profileUpdateRequestDto.getAgeGroup());
    }


    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateUserProfileImage(final Long userId, final MultipartFile profileImage) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        removeExistProfileImage(user.getProfileImage());

        if (Objects.isNull(profileImage) || profileImage.isEmpty()) {
            user.changeProfileImage(null);
        } else {
            String newFile = fileUploader.saveFile(user.getId(), extractMultipartFileInfo(profileImage));
            user.changeProfileImage(newFile);
        }
    }

    private FileDto extractMultipartFileInfo(MultipartFile profileImage) throws IOException {
        String originalFilename = profileImage.getOriginalFilename();
        long size = profileImage.getSize();
        String contentType = profileImage.getContentType();
        InputStream inputStream = profileImage.getInputStream();
        return new FileDto(originalFilename, size, contentType, inputStream);
    }

    private void removeExistProfileImage(final String userProfileImage) throws IOException {

        if (Objects.nonNull(userProfileImage)) {
            fileUploader.deleteFile(userProfileImage);
        }
    }

}
