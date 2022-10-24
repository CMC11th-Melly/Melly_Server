package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetScrapedMemoryCond {
    private GroupType groupType;
}
