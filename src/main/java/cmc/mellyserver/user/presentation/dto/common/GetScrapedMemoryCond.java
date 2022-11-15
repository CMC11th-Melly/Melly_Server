package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScrapedMemoryCond {
    private GroupType groupType;
}
