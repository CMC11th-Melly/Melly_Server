package cmc.mellyserver.mellycore.common.aws;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    List<String> saveFileList(Long userId, List<MultipartFile> multipartFiles);

    String saveFile(Long userId, MultipartFile file);

    void deleteFile(String fileName) throws IOException;

    Long calculateImageVolume(String bucketName, String username);
}
