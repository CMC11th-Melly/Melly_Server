package cmc.mellyserver.auth.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class GoogleUserResponse {

    private String id;
    private String email;
    @Nullable
    private Boolean verified_email;
    @Nullable
    private String name;
    @Nullable
    private String given_name;
    @Nullable
    private String family_name;
    private String picture;
    @Nullable
    private String locale;
}
