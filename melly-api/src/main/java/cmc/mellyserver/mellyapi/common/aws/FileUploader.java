package cmc.mellyserver.mellyapi.common.aws;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

	List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles);

	String getMultipartFileName(MultipartFile file);

}
