package cmc.mellyserver.mellyapi.controller.auth.dto.response;

import cmc.mellyserver.mellyapi.domain.auth.dto.response.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponseDto<T> {

    public TokenResponseDto tokenInfo;

    public T data;
}
