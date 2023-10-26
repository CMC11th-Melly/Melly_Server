package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.model.KakaoResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoLogin", url = "${client.oauth.uri.kakao}")
interface KakaoLoginApi {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	KakaoResource call(@RequestHeader(value = "Authorization") String accessToken);

}
