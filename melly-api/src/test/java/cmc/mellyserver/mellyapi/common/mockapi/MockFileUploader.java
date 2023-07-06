package cmc.mellyserver.mellyapi.common.mockapi;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MockFileUploader implements FileUploader {
    @Override
    public List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles) {
        return List.of("file1.png");
    }

    @Override
    public String getMultipartFileName(MultipartFile file) {
        return "file.png";
    }
}
