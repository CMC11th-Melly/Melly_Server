package cmc.mellyserver.auth.client;

import cmc.mellyserver.auth.client.dto.GoogleUserResponse;
import cmc.mellyserver.auth.exception.TokenValidFailedException;
import cmc.mellyserver.user.domain.User;
import com.amazonaws.auth.policy.Resource;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleClient implements Client{

    private final WebClient webClient;
    @Override
    public User getUserData(String accessToken) {

        JSONObject jsonObject = decodeFromIdToken(accessToken);
        System.out.println("최종 응답값 : " + jsonObject.toJSONString());
        String sub = (String)jsonObject.get("sub");
        String email = (String)jsonObject.get("email");

        return User.builder().userId(sub).email(email).build();

    }
    public JSONObject decodeFromIdToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            JWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            Map<String, Object> stringObjectMap = getPayload.toJSONObject();
            for (String s : stringObjectMap.keySet()) {
                System.out.println("무슨 값이 들어있지? " + stringObjectMap.get(s));
            }
            JSONObject payload = new JSONObject(stringObjectMap);

            if (payload != null) {
                return payload;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
