package cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign;

import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.KakaoUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoOpenFeign", url = "${oauth.resource.uri.kakao}")
public interface KakaoOpenFeign {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    KakaoUserData call(
            @RequestHeader(value = "Authorization") String accessToken
    );
}

