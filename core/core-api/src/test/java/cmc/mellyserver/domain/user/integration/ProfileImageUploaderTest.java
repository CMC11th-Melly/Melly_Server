package cmc.mellyserver.domain.user.integration;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.user.ProfileImageUploader;
import cmc.mellyserver.support.IntegrationTestSupport;

public class ProfileImageUploaderTest extends IntegrationTestSupport {

  @Autowired
  private ProfileImageUploader profileImageUploader;

  @Test
  @DisplayName("사용자가 프로필 이미지를 기본으로 변경하면 서버와 S3에서 삭제한다")
  void 사용자가_프로필이미지를_기본으로_변경하면_서버와_S3에서_삭제한다() throws IOException {

	// given
	User 모카 = 모카();

	// when
	profileImageUploader.update(모카, null, true);

	// then
	assertThat(모카.getProfileImage()).isNull();
  }

  @Test
  @DisplayName("사용자가 프로필 이미지를 수정하면 서버와 S3에 등록한다")
  void 사용자가_프로필이미지를_수정하면_서버와_S3에서_등록한다() throws IOException {

	// given
	User 모카 = 모카();
	MockMultipartFile mockMultipartFile = new MockMultipartFile("프로필 수정", "프로필 수정".getBytes());

	// when
	profileImageUploader.update(모카, mockMultipartFile, false);

	// then
	assertThat(모카.getProfileImage()).isEqualTo("수정된프로필.jpg");
  }
}
