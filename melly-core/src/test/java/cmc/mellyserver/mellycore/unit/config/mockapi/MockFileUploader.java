package cmc.mellyserver.mellycore.unit.config.mockapi;

import cmc.mellyserver.mellycore.common.aws.StorageService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MockFileUploader implements StorageService {
    @Override
    public List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles) {
        return List.of("file1.png");
    }

    @Override
    public String getMultipartFileName(MultipartFile file) {
        return "file.png";
    }
}
