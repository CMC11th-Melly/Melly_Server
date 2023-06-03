package cmc.mellyserver.mellyapi.comment.presentation.dto.request;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeRequest {

	private Long commentId;

	@Nullable
	private Long userId;
}
