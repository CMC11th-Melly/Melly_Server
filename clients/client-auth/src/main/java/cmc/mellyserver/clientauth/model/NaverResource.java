package cmc.mellyserver.clientauth.model;

import cmc.mellyserver.clientauth.api.LoginClientResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaverResource {

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

    public LoginClientResult toResult() {
        return new LoginClientResult(response.id, "naver");
    }
}
