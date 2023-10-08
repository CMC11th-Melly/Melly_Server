package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.dto.GoogleUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleLogin", url = "${client.oauth.uri.google}")
interface GoogleLoginApi {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    GoogleUserData call(
            @RequestParam(value = "id_token") String accessToken
    );
}

