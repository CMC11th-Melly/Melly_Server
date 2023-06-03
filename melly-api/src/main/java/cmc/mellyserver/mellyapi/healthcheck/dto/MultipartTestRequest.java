package cmc.mellyserver.mellyapi.healthcheck.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultipartTestRequest {

	private List<MultipartFile> image;
}
