package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class GroupCreateRequest {

    @NotNull
    @Size(max = 23,message = "그룹명은 23자 이하입니다.")
    private String groupName;

    @NotNull
    private GroupType groupType;

    @Schema(example = "1부터 10까지 int형 숫자 중 하나 넣어주세용")
    private int groupIcon;

    @Builder
    public GroupCreateRequest(String groupName, GroupType groupType,int groupIcon) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }
}
