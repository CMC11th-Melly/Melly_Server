package cmc.mellyserver.clientauth.api;


import cmc.mellyserver.clientauth.LoginClient;
import cmc.mellyserver.clientauth.dto.AppleUserData;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import static cmc.mellyserver.dbcore.user.enums.Provider.APPLE;


@Slf4j
@Component
@RequiredArgsConstructor
public class AppleClient implements LoginClient {

    private final AppleLoginApi appleOpenFeign;

    @Override
    public boolean supports(Provider provider) {
        return provider == APPLE;
    }

    @Override
    public User getUserData(String accessToken) {

        AppleUserData response = appleOpenFeign.call();

        try {

            String headerOfIdentityToken = accessToken.substring(0, accessToken.indexOf("."));

            Map<String, String> header = new ObjectMapper().readValue(
                    new String(Base64.getDecoder().decode(headerOfIdentityToken),
                            "UTF-8"), Map.class);

            AppleUserData.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new IllegalAccessException());

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Claims body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody();

            return User.createOauthLoginUser(body.getSubject(), APPLE, null, null, null, null);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
