package cmc.mellyserver.mellyapi.controller.auth.dto.common;

import cmc.mellyserver.mellyapi.controller.auth.dto.response.AccessTokenUserData;
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
