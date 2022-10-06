package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfoRequest {

    @Schema(example = "34.0432423")
    private Double lat;
    @Schema(example = "127.0454544")
    private Double lng;
    @Schema(example = "진짜 인생 술집")
    private String title;
    @Schema(example = "동기들 데리고 꼭 다시 와볼만한 술집")
    private String content;
    @Schema(example = "melly.png, cmc11th.jpg")
    private List<MultipartFile> images;
    @Schema(example = "1")
    private Long groupId;
    private GroupType groupType;
    @Schema(example = "5",description = "4.5같이 소수점도 필요하기 때문에 int 대신 Long 사용")
    private Long star;

}
