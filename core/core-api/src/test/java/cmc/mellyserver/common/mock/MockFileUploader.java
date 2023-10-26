package cmc.mellyserver.common.mock;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test")
public class MockFileUploader implements StorageService {

	@Override
	public List<String> saveFileList(Long userId, List<FileDto> multipartFiles) {
		return null;
	}

	@Override
	public String saveFile(Long userId, FileDto file) {
		return "mock.jpg";
	}

	@Override
	public void deleteFile(String fileName) {
	}

	@Override
	public Long calculateImageVolume(String username) {
		return 1L;
	}

}
