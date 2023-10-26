package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.model.AppleResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleLogin", url = "${client.oauth.uri.apple}")
public interface AppleLoginApi {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	AppleResource call();

}
