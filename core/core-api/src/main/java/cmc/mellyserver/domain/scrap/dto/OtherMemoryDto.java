package cmc.mellyserver.domain.scrap.dto;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OtherMemoryDto {

	private Long memoryId;

	private GroupType groupType;

	private List<MemoryImageDto> memoryImages;

	private String title;

	private List<String> keyword;

	private String createdDate;

}
