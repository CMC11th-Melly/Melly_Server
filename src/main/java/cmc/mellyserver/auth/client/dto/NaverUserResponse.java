package cmc.mellyserver.auth.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserResponse {
    private String resultcode;
    private String message;
    private Response response;

    @Data
    @NoArgsConstructor
    public class Response{

        private String id;
        private String email;
        private String nickname;



    }
}
