package cmc.mellyserver.mellyapi.auth.application.loginclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverUserData {

    private String resultcode;

    private String message;

    private Response response;

    @Data
    @NoArgsConstructor
    public class Response {

        private String id;

        private String email;

        private String nickname;
    }
}
