package cmc.mellyserver.mellyinfra.aws;

import cmc.mellyserver.mellycore.common.aws.FileUploader;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
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
public class S3FileLoader implements FileUploader {

    private final AWSS3UploadService uploadService;

    @Override
    public List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles) {

        if (multipartFiles != null) {
            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file -> {
                String fileName = createFileNames(uid, file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try (InputStream inputStream = file.getInputStream()) {
                    uploadService.uploadFile(inputStream, objectMetadata, fileName);
                } catch (IOException e) {
                    throw new IllegalArgumentException();
                }
                fileNameList.add(uploadService.getFileUrl(fileName));
            });
            return fileNameList;
        }
        return null;
    }

    @Override
    public String getMultipartFileName(MultipartFile file) {

        if (file != null) {
            String fileNameList;
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                uploadService.uploadFile(inputStream, objectMetadata, fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException();
            }
            fileNameList = uploadService.getFileUrl(fileName);

            return fileNameList;
        }
        return null;
    }

    private String createFileNames(String uid, String fileName) {

        return uid + "/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String createFileName(String fileName) {

        return "profile/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {

        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }
}
