package cmc.mellyserver.user.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationOnOffResponseDto {

    private boolean enableAppPush;
    private boolean enableContentLike;
    private boolean enableContent;
}
