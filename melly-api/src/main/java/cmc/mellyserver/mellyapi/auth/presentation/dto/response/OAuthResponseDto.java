package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import cmc.mellyserver.mellyapi.auth.application.dto.response.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OAuthResponseDto<T> {

    public TokenResponseDto tokenInfo;

    public T data;
}
