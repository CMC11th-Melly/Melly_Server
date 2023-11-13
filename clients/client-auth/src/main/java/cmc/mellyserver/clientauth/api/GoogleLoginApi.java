package cmc.mellyserver.clientauth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmc.mellyserver.clientauth.model.GoogleResource;

@FeignClient(name = "googleLogin", url = "${client.oauth.uri.google}")
interface GoogleLoginApi {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  GoogleResource call(@RequestParam(value = "id_token") String accessToken);
}
