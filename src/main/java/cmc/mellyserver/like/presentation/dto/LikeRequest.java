package cmc.mellyserver.like.presentation.dto;

import cmc.mellyserver.like.domain.LikeType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeRequest {
    private Long memoryId;
    private LikeType likeType;
}
