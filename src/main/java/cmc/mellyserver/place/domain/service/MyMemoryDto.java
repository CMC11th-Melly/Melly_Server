package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMemoryDto {
    private GroupType groupType;
    private List<MemoryImageDto> memoryImages;
    private String title;
    private String keyword;
    private String createdDate;
}
