package cmc.mellyserver.domain.user;

import java.io.IOException;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.FileService;
import cmc.mellyserver.dbcore.user.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileImageUploader {

  private final FileService fileService;

  public void update(User user, MultipartFile profileImage, boolean isDeleted) throws IOException {

	if (isDeleted) {
	  removeProfileImage(user);
	}

	if (Objects.nonNull(profileImage)) {
	  storeProfileImage(user, profileImage);
	}
  }

  public int calculateImageVolume(String email) {
	return fileService.calculateImageVolume(email).intValue();
  }

  private void storeProfileImage(User user, MultipartFile profileImage) throws IOException {
	fileService.deleteFile(user.getProfileImage());
	String profileImageUrl = fileService.saveFile(user.getId(), extractImageInfo(profileImage));
	user.changeProfileImage(profileImageUrl);
  }

  private void removeProfileImage(User user) throws IOException {
	fileService.deleteFile(user.getProfileImage());
	user.removeProfileImage();
  }

  private FileDto extractImageInfo(MultipartFile profileImage) throws IOException {
	return new FileDto(profileImage.getOriginalFilename(), profileImage.getSize(), profileImage.getContentType(),
		profileImage.getInputStream());
  }
}
