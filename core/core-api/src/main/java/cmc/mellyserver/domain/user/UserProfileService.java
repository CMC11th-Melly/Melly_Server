package cmc.mellyserver.domain.user;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.StorageService;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.domain.user.dto.response.ProfileUpdateRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserReader userReader;

    private final StorageService fileUploader;


    @Cacheable(value = "image-volume:user-id", key = "#userId")
    public int checkImageStorageVolume(final Long userId) {

        User user = userReader.findById(userId);
        return fileUploader.calculateImageVolume(user.getEmail()).intValue();
    }

    @CircuitBreaker(name = "user-profile", fallbackMethod = "userProfileFallback")
    @Cacheable(value = "profile:user-id", key = "#userId")
    public ProfileResponseDto getProfile(final Long userId) {

        User user = userReader.findById(userId);
        return ProfileResponseDto.of(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }

    public ProfileResponseDto userProfileFallback(final Long userId) {
        User user = userReader.findById(userId);
        return ProfileResponseDto.of(user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }


    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateProfile(final Long userId, final ProfileUpdateRequestDto profileUpdateRequestDto) {

        User user = userReader.findById(userId);
        user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(), profileUpdateRequestDto.getAgeGroup());
    }


    @CachePut(value = "profile:user-id", key = "#userId")
    @Transactional
    public void updateProfileImage(final Long userId, MultipartFile profileImage) throws IOException {

        User user = userReader.findById(userId);
        removeExistProfileImage(user); // 기존에 등록된 이미지가 있다면 삭제한다

        if (Objects.nonNull(profileImage)) {
            user.changeProfileImage(fileUploader.saveFile(user.getId(), extractFileInfo(profileImage)));
        }
    }


    private FileDto extractFileInfo(MultipartFile profileImage) throws IOException {

        return new FileDto(profileImage.getOriginalFilename(), profileImage.getSize(), profileImage.getContentType(), profileImage.getInputStream());
    }


    private void removeExistProfileImage(User user) throws IOException {

        if (Objects.nonNull(user.getProfileImage())) {

            fileUploader.deleteFile(user.getProfileImage());
            user.changeProfileImage(null);
        }
    }
}
