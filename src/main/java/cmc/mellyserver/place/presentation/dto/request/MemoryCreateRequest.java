package cmc.mellyserver.place.presentation.dto.request;

import cmc.mellyserver.common.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemoryCreateRequest {

    private Double lat;

    private Double lng;

    private String placeName;

    private String placeCategory;

    @NotNull
    @Length(max = 23, message = "제목은 23자 이하로 입력해주세요.")
    private String title;

    @NotNull
    @Length(min = 20, message = "본문은 20자 이상으로 작성해주세요.")   // ok
    @Length(max = 650, message = "본문은 650자 이하로 작성해주세요.")   // ok
    private String content;

    private List<String> keyword;

    private Long groupId;

    private OpenType openType;

    @JsonFormat(pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

    private Long star;

}