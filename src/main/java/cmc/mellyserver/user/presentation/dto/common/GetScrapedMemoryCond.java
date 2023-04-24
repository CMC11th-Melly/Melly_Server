package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.common.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScrapedMemoryCond {
    private GroupType groupType;
}
