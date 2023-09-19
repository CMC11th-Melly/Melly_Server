package cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign;

import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.NaverUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverOpenFeign", url = "${oauth.resource.uri.naver}")
public interface NaverOpenFeign {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    NaverUserData call(
            @RequestHeader(value = "Authorization") String accessToken
    );
}

