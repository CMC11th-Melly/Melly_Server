package cmc.mellyserver.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ProfileUpdateRequest {

    private String nickname;
    private MultipartFile image;
}
