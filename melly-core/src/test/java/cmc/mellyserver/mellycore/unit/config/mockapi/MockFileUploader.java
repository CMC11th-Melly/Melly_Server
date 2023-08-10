package cmc.mellyserver.mellycore.unit.config.mockapi;


import cmc.mellyserver.mellycore.infrastructure.storage.StorageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class MockFileUploader implements StorageService {

    @Override
    public List<String> saveFileList(Long userId, List<MultipartFile> multipartFiles) {
        return List.of("file1.png");
    }

    @Override
    public String saveFile(Long userId, MultipartFile file) {
        return "file.png";
    }

    @Override
    public void deleteFile(String fileName) throws IOException {

    }

    @Override
    public Long calculateImageVolume(String username) {
        return null;
    }
}
