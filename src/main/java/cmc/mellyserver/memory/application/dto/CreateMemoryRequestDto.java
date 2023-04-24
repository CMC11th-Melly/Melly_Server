package cmc.mellyserver.memory.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateMemoryRequestDto {
    String uid;
    Double lat;
    Double lng;
    String title;
    String placeName;
    String placeCategory;
    String content;
    Long star;
    Long groupId;
    GroupType groupType;
    List<String> keyword;
    List<MultipartFile> multipartFiles;
}
