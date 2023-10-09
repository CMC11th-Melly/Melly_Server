package cmc.mellyserver.domain.auth;


import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.dbcore.user.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class LoginClientFactory {

    // LoginClient 타입의 모든게 다 들어온다.
    private final List<LoginClient> loginClientList;

    private final Map<Provider, LoginClient> factoryCache = new HashMap<>();

    public LoginClient find(Provider provider) {
        LoginClient loginClient = factoryCache.get(provider);
        if (loginClient != null) {
            return loginClient;
        }

        // 이 부분이 중요하다.
//        loginClient = loginClientList.stream()
//                .filter(v -> v.supports(provider))
//                .findFirst()
//                .orElseThrow();

        loginClient = loginClientList.stream().filter(client ->
                        client.supports(provider.name()))
                .findFirst()
                .orElseThrow();

        factoryCache.put(provider, loginClient);
        return loginClient;
    }
}
