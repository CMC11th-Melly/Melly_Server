package cmc.mellyserver.auth.application.impl.client.dto;

import lombok.*;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleUserData {
    private List<Key> keys;

    @Getter
    @Setter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    public Optional<Key> getMatchedKeyBy(String kid, String alg) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }
}