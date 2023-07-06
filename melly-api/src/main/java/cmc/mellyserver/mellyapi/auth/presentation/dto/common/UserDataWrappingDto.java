package cmc.mellyserver.mellyapi.auth.presentation.dto.common;

import cmc.mellyserver.mellyapi.auth.presentation.dto.response.AccessTokenUserData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserDataWrappingDto {

    private AccessTokenUserData user;
}
