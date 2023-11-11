package cmc.mellyserver.clientauth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import cmc.mellyserver.clientauth.model.NaverResource;

/*
통신 클라이언트를 호출하는 작업은 OpenFeign 인터페이스가 아닌 NaverClient라는 객체를 통해 수행합니다.
OpenFeign 인터페이스에 대한 세부 구현은 package default 접근 지시자를 통해 예상치 못한 곳에서의 사용을 차단했습니다.
 */
@FeignClient(name = "naverLogin", url = "${client.oauth.uri.naver}")
interface NaverLoginApi {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	NaverResource call(@RequestHeader(value = "Authorization") String accessToken);
}
