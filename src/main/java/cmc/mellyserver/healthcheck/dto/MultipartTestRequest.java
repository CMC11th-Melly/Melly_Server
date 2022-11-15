package cmc.mellyserver.healthcheck.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipartTestRequest {

    private List<MultipartFile> image;
}
