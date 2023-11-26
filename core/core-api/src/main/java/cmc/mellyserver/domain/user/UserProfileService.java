package cmc.mellyserver.domain.user;

import java.io.IOException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.dto.request.ProfileUpdateRequestDto;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserReader userReader;

    private final UserWriter userWriter;

    private final ProfileImageUploader profileImageUploader;

    @Cacheable(cacheNames = CacheNames.USER, key = "#userId")
    public ProfileResponseDto getProfile(final Long userId) {

        return ProfileResponseDto.of(userReader.findById(userId));
    }

    @CacheEvict(cacheNames = CacheNames.USER, key = "#userId")
    @Transactional
    public void updateProfile(final Long userId, final ProfileUpdateRequestDto profileUpdateRequestDto) {

        userWriter.update(userId, profileUpdateRequestDto);
    }

    @CacheEvict(cacheNames = CacheNames.USER, key = "#userId")
    @Transactional
    public void updateProfileImage(final Long userId, MultipartFile newProfileImage, boolean isDeleted) throws
        IOException {

        User user = userReader.findById(userId);
        profileImageUploader.update(user, newProfileImage, isDeleted);
    }
}
