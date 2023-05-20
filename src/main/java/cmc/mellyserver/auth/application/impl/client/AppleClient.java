package cmc.mellyserver.auth.application.impl.client;


import cmc.mellyserver.auth.application.LoginClient;
import cmc.mellyserver.auth.application.impl.client.dto.AppleUserData;
import cmc.mellyserver.common.enums.Provider;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.exception.GlobalServerException;
import cmc.mellyserver.common.enums.RoleType;
import cmc.mellyserver.user.domain.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleClient implements LoginClient {

    private final WebClient webClient;

    @Override
    public boolean supports(Provider provider) {
        return provider == Provider.APPLE;
    }

    @Override
    public User getUserData(String accessToken) {

        AppleUserData response = webClient.get()
                .uri("https://appleid.apple.com/auth/keys")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new GlobalBadRequestException(ExceptionCodeAndDetails.APPLE_ACCESS)))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new GlobalServerException()))
                .bodyToMono(AppleUserData.class)
                .block();

        try {

            String headerOfIdentityToken = accessToken.substring(0, accessToken.indexOf("."));

            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken),
                                                          "UTF-8"), Map.class);

            AppleUserData.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new GlobalServerException());

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Claims body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody();

            return User.builder()
                        .uid(body.getSubject())
                        .password("NO_PASSWORD")
                        .provider(Provider.APPLE)
                        .roleType(RoleType.USER)
                        .build();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        }
        catch (ExpiredJwtException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
