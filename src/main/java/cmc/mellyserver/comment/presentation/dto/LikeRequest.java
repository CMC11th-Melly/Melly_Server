package cmc.mellyserver.comment.presentation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class LikeRequest {

    private Long commentId;

    @Nullable
    private Long userId;
}
