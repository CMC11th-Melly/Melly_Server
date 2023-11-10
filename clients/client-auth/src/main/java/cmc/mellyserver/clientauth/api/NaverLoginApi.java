package cmc.mellyserver.clientauth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import cmc.mellyserver.clientauth.model.NaverResource;

@FeignClient(name = "naverLogin", url = "${client.oauth.uri.naver}")
interface NaverLoginApi {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	NaverResource call(@RequestHeader(value = "Authorization") String accessToken);
}
