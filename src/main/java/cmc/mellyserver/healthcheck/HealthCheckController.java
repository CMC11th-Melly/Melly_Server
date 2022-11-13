package cmc.mellyserver.healthcheck;

import cmc.mellyserver.common.exception.GlobalServerException;
import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.common.util.aws.AWSS3UploadService;
import cmc.mellyserver.healthcheck.dto.MultipartTestRequest;
import cmc.mellyserver.healthcheck.dto.PwEncode;
import cmc.mellyserver.user.application.UserService;
import cmc.mellyserver.user.domain.UserRepository;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthCheckController {

    private final AWSS3UploadService uploadService;


    @Operation(summary = "헬스 체크용 API")
    @GetMapping("/health")
    public String healthCheck()
    {
        return "떡잎마을방범대 파이어~";
    }



    @Operation(summary = "액세스 토큰 Authorization Header에 추가 시 인증 통과 테스트")
    @GetMapping("/authTest")
    public ResponseEntity<CommonResponse> authCheck()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(new CommonResponse(200,"정상적으로 인증되었습니다"));
    }



    @Operation(summary = "멀티파트 통신 테스트")
    @PostMapping("/imageTest")
    public ResponseEntity<List<String>> multipartTest(MultipartTestRequest multipartTestRequest)
    {
        List<String> multipartFileNames = getMultipartFileNames(multipartTestRequest.getImage());
        return ResponseEntity.ok(multipartFileNames);
    }



    private List<String> getMultipartFileNames(List<MultipartFile> multipartFiles) {

        if(multipartFiles != null)
        {
            List<String> fileNameList = new ArrayList<>();

            multipartFiles.forEach(file->{
                String fileName = createFileName(file.getOriginalFilename());
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



    private String createFileName(String fileName) {
        return "user1/" + UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }



    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("잘못된 형식 입니다.");
        }
    }

}
