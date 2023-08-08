package cmc.mellyserver.mellyinfra.client;


import cmc.mellyserver.mellycore.common.enums.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginClientFactory {

    private final List<LoginClient> loginClientList;

    private final Map<Provider, LoginClient> factoryCache = new HashMap<>();

    public LoginClient find(Provider provider) {
        LoginClient loginClient = factoryCache.get(provider);
        if (loginClient != null) {
            return loginClient;
        }

        loginClient = loginClientList.stream()
                .filter(v -> v.supports(provider))
                .findFirst()
                .orElseThrow();

        factoryCache.put(provider, loginClient);
        return loginClient;
    }
}
