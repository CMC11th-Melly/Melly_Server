package cmc.mellyserver.clientauth.api;

import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.model.AppleResource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import static cmc.mellyserver.clientauth.api.Provider.APPLE;


@Component
@RequiredArgsConstructor
public class AppleClient implements LoginClient {

    private final AppleLoginApi appleLoginApi;

    @Override
    public boolean supports(String provider) {
        return provider.equals(APPLE);
    }

    @Override
    public LoginClientResult getUserData(String accessToken) {

        AppleResource response = appleLoginApi.call();

        try {
            Claims body = extractClaims(accessToken, response);
            return new LoginClientResult(body.getSubject(), APPLE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Claims extractClaims(String accessToken, AppleResource response) throws JsonProcessingException, UnsupportedEncodingException, IllegalAccessException, NoSuchAlgorithmException, InvalidKeySpecException {
        String headerOfIdentityToken = accessToken.substring(0, accessToken.indexOf("."));

        Map<String, String> header = new ObjectMapper().readValue(
                new String(Base64.getDecoder().decode(headerOfIdentityToken),
                        "UTF-8"), Map.class);

        AppleResource.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                .orElseThrow(() -> new IllegalAccessException());

        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody();
    }
}
