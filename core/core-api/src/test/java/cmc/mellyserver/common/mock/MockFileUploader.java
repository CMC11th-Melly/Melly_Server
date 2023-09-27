package cmc.mellyserver.common.mock;

import cmc.mellyserver.mellycore.infrastructure.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
@Profile("test")
public class MockFileUploader implements StorageService {

    @Override
    public List<String> saveFileList(Long userId, List<MultipartFile> multipartFiles) {
        return List.of("test_file.jpg");
    }

    @Override
    public String saveFile(Long userId, MultipartFile file) {
        return file.getName();
    }

    @Override
    public void deleteFile(String fileName) {
    }

    @Override
    public Long calculateImageVolume(String username) {
        return 1L;
    }
}
