package cmc.mellyserver.mellyapi.auth.presentation.dto.response;

import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserExistResponseDto {

    private Provider provider;

    private String email;
}
