package cmc.mellyserver.mellycore.common.aws;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploader {

    List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles);

    String getMultipartFileName(MultipartFile file);
}
