package cmc.mellyserver.mellyappexternalapi.common.mockapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.mellyappexternalapi.common.aws.FileUploader;

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
