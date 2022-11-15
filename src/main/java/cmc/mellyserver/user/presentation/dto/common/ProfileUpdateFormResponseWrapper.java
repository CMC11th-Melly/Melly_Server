package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileUpdateFormResponseWrapper {

    private ProfileUpdateFormResponse userInfo;
}
