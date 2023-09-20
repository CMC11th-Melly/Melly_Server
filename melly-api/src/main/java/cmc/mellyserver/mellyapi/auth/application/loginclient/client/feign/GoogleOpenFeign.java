package cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign;

import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.GoogleUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "googleOpenFeign", url = "${oauth.resource.uri.google}")
public interface GoogleOpenFeign {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    GoogleUserData call(
            @RequestParam(value = "id_token") String accessToken
    );
}

