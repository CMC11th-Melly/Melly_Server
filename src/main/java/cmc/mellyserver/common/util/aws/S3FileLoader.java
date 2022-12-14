package cmc.mellyserver.common.util.aws;

import cmc.mellyserver.common.exception.GlobalServerException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileLoader {
    private final AWSS3UploadService uploadService;

    public List<String> getMultipartFileNames(String uid, List<MultipartFile> multipartFiles) {

        if(multipartFiles != null)
        {
            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file->{
                String fileName = createFileNames(uid , file.getOriginalFilename());
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                objectMetadata.setContentType(file.getContentType());

                try(InputStream inputStream = file.getInputStream()) {
                    uploadService.uploadFile(inputStream,objectMetadata,fileName);
                } catch(IOException e) {
                    throw new GlobalServerException();
                }
                fileNameList.add(uploadService.getFileUrl(fileName));
            });
            return fileNameList;
        }
        return null;
    }

    public String getMultipartFileName(MultipartFile file) {

        if(file != null)
        {
            String fileNameList;
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                uploadService.uploadFile(inputStream,objectMetadata,fileName);
            } catch(IOException e) {
                throw new GlobalServerException();
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
        return  "profile/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file ????????? ????????? ????????? ???????????? ?????? ???????????? ????????????, ?????? ????????? ???????????? ???????????? ??? ?????? ?????? ?????? .??? ?????? ????????? ?????????????????????.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("????????? ?????? ?????????.");
        }
    }






}
