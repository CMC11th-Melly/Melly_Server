package cmc.mellyserver.comment.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeRequest {
    private Long commentId;
}
