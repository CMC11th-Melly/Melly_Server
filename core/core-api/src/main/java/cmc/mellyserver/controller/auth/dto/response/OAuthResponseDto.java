package cmc.mellyserver.controller.auth.dto.response;

import cmc.mellyserver.domain.auth.dto.response.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponseDto<T> {

    public TokenResponseDto tokenInfo;

    public T data;
}
