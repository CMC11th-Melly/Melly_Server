package cmc.mellyserver.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginClientFactory {

	private final List<LoginClient> loginClientList;

	/*
	한번 생성된 Client 객체를 런타임 내에 메모리상에 캐싱합니다
	 */
	private final Map<Provider, LoginClient> factoryCache = new HashMap<>();

	public LoginClient find(Provider provider) {
		LoginClient loginClient = factoryCache.get(provider);
		if (loginClient != null) {
			return loginClient;
		}

		loginClient = loginClientList.stream()
			.filter(client -> client.supports(provider.name()))
			.findFirst()
			.orElseThrow();

		factoryCache.put(provider, loginClient);
		return loginClient;
	}

}
