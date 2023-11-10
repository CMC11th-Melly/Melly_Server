package cmc.mellyserver.domain.user;

import java.io.IOException;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.dto.response.ProfileResponseDto;
import cmc.mellyserver.domain.user.dto.response.ProfileUpdateRequestDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

	private final UserReader userReader;

	private final ProfileImageUploader profileImageUploader;

	@Cacheable(value = "image-volume:user-id", key = "#userId")
	public int calculateImageTotalVolume(final Long userId) {

		User user = userReader.findById(userId);
		return profileImageUploader.calculateImageVolume(user.getEmail());
	}

	@Cacheable(value = "profile:user-id", key = "#userId")
	public ProfileResponseDto getProfile(final Long userId) {

		return ProfileResponseDto.of(userReader.findById(userId));
	}

	@CachePut(value = "profile:user-id", key = "#userId")
	@Transactional
	public void updateProfile(final Long userId, final ProfileUpdateRequestDto profileUpdateRequestDto) {

		User user = userReader.findById(userId);
		user.updateProfile(profileUpdateRequestDto.getNickname(), profileUpdateRequestDto.getGender(),
			profileUpdateRequestDto.getAgeGroup());
	}

	@CachePut(value = "image-volume:user-id", key = "#userId")
	@Transactional
	public void updateProfileImage(final Long userId, MultipartFile newProfileImage, boolean isDeleted) throws
		IOException {

		User user = userReader.findById(userId);
		profileImageUploader.update(user, newProfileImage, isDeleted);
	}
}
