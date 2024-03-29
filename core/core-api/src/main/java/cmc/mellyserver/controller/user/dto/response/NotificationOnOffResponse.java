package cmc.mellyserver.controller.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationOnOffResponse {

    private boolean enableAppPush;

    private boolean enableContentLike;

    private boolean enableContent;

}
