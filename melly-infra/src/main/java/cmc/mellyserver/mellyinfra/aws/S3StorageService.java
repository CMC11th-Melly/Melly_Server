package cmc.mellyserver.mellyinfra.aws;

import cmc.mellyserver.mellycore.common.port.aws.StorageService;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Profile({"local", "prod"})
@Component
@RequiredArgsConstructor
public class S3StorageService implements StorageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.cloud-front-url}")
    private String CLOUD_FRONT_URL;
    private final AmazonS3Client amazonS3Client;


    @Override
    public String saveFile(Long userId, MultipartFile file) {

        if (!file.isEmpty()) {

            String fileName = createFileName(userId, file.getOriginalFilename());

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }

            return getFileUrl(fileName);
        }

        return null;
    }

    @Override
    public List<String> saveFileList(Long userId, List<MultipartFile> multipartFiles) {

        if (!multipartFiles.isEmpty()) {

            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file -> {

                String fileName = createFileName(userId, file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try (InputStream inputStream = file.getInputStream()) {
                    uploadFile(inputStream, objectMetadata, fileName);
                } catch (IOException e) {
                    throw new IllegalArgumentException();
                }
                fileNameList.add(getFileUrl(fileName));
            });

            return fileNameList;
        }

        return null;
    }


    @Override
    public void deleteFile(String fileName) throws IOException {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (SdkClientException e) {
            throw new IOException("s3 exception occur", e);
        }
    }

    @Override
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

        amazonS3Client.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String getFileUrl(String filename) {
        return CLOUD_FRONT_URL + "/" + filename;
    }

}
