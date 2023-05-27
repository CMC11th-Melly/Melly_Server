package cmc.mellyserver.user.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipateGroupRequest {
    private Long groupId;

    public ParticipateGroupRequest(Long groupId) {
        this.groupId = groupId;
    }
}
