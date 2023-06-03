package cmc.mellyserver.mellyapi.common.mockapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.mellyapi.common.aws.FileUploader;

public class MockFileUploader implements FileUploader {
	@Override
	public List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles) {
		return List.of("file1.png", "file2.png");
	}

	@Override
	public String getMultipartFileName(MultipartFile file) {
		return "file.png";
	}
}
