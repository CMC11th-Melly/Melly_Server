package cmc.mellyserver.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import cmc.mellyserver.FileDto;
import cmc.mellyserver.FileService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile(value = {"local", "prod"})
class S3StorageService implements FileService {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.cloud-front-url}")
	private String CLOUD_FRONT_URL;

	private final AmazonS3 amazonS3Client;

	public List<String> saveFileList(Long userId, List<FileDto> files) {

		List<String> fileNameList = new ArrayList<>();

		files.forEach(file -> {
			String fileName = saveFile(userId, file);
			fileNameList.add(getFileUrl(fileName));
		});

		return fileNameList;
	}

	public String saveFile(Long userId, FileDto file) {

		String fileName = createFileName(userId, file.getOriginalFilename());
		ObjectMetadata objectMetadata = setupS3Object(file);

		try (InputStream inputStream = file.getInputStream()) {
			uploadFile(inputStream, objectMetadata, fileName);
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}

		return getFileUrl(fileName);
	}

	public void deleteFile(String fileName) throws IOException {
		try {
			amazonS3Client.deleteObject(bucket, fileName);
		} catch (SdkClientException e) {
			throw new IOException();
		}
	}

	@Override
	public void deleteFileList(List<Long> deleteFileIds) {

	}

	public Long calculateImageVolume(String username) {

		ObjectListing mellyimage = amazonS3Client.listObjects(bucket, username);
		List<S3ObjectSummary> objectSummaries = mellyimage.getObjectSummaries();

		return objectSummaries.stream().mapToLong(S3ObjectSummary::getSize).sum();

	}

	private String createFileName(Long userId, String fileName) {

		return "dev/" + userId + "/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	private String getFileExtension(String fileName) {

		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("잘못된 형식 입니다.");
		}
	}

	private void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String filename) {

		amazonS3Client.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private String getFileUrl(String filename) {
		return CLOUD_FRONT_URL + "/" + filename;
	}

	private ObjectMetadata setupS3Object(FileDto file) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());
		return objectMetadata;
	}

}
