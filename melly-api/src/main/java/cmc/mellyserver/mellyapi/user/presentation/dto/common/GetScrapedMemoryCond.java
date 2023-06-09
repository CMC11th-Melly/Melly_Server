package cmc.mellyserver.mellyapi.user.presentation.dto.common;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScrapedMemoryCond {

    private GroupType groupType;
}
