package cmc.mellyserver.auth.controller.dto.response;

import cmc.mellyserver.auth.dto.response.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponseDto<T> {

    public TokenResponseDto tokenInfo;

    public T data;
}
