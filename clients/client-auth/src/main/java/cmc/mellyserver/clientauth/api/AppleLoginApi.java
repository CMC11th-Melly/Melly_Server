package cmc.mellyserver.clientauth.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import cmc.mellyserver.clientauth.model.AppleResource;

@FeignClient(name = "appleLogin", url = "${client.oauth.uri.apple}")
interface AppleLoginApi {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  AppleResource call();
}
