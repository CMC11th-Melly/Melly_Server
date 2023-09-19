package cmc.mellyserver.mellyapi.auth.application.loginclient.client.feign;

import cmc.mellyserver.mellyapi.auth.application.loginclient.dto.AppleUserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "appleOpenFeign", url = "${oauth.resource.uri.apple}")
public interface AppleOpenFeign {

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    AppleUserData call();
}

