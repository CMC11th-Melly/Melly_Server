package cmc.mellyserver.mellyapi.auth.application.impl.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import cmc.mellyserver.mellyapi.auth.application.LoginClient;
import cmc.mellyserver.mellyapi.auth.application.impl.client.dto.GoogleUserData;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.common.exception.TokenValidFailedException;
import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.common.enums.RoleType;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleClient implements LoginClient {

	private final WebClient webClient;

	@Override
	public boolean supports(Provider provider) {
		return provider == Provider.GOOGLE;
	}

	@Override
	public User getUserData(String accessToken) {

		GoogleUserData googleUserResponse = webClient.get()
			.uri("https://oauth2.googleapis.com/tokeninfo",
				builder -> builder.queryParam("id_token", accessToken).build())
			.retrieve()
			.onStatus(HttpStatus::is4xxClientError,
				response -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.GOOGLE_ACCESS)))
			.onStatus(HttpStatus::is5xxServerError,
				response -> Mono.error(new TokenValidFailedException("Internal Server Error")))
			.bodyToMono(GoogleUserData.class)
			.block();

		return User.builder()
			.uid(googleUserResponse.getSub())
			.provider(Provider.GOOGLE)
			.password("NO_PASSWORD")
			.roleType(RoleType.USER).build();

	}
}
