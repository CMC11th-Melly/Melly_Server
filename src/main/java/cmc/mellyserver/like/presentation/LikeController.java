package cmc.mellyserver.like.presentation;

import cmc.mellyserver.common.response.CommonResponse;
import cmc.mellyserver.like.application.LikeService;
import cmc.mellyserver.like.presentation.dto.LikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 추가")
    @PostMapping
    public ResponseEntity<CommonResponse> addLike(@RequestBody LikeRequest likeRequest)
    {
          likeService.addLike(likeRequest.getMemoryId(),likeRequest.getLikeType());
          return ResponseEntity.ok(new CommonResponse(200,"좋아요 추가 완료"));
    }

}
