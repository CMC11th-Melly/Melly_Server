package cmc.mellyserver.controller.comment.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeRequest {

    private Long commentId;

    private Long userId;

    @Builder
    public LikeRequest(Long commentId, Long userId) {
        this.commentId = commentId;
        this.userId = userId;
    }

}
