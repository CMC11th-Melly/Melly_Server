package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.common;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScrapedMemoryCond {

    private GroupType groupType;
}
