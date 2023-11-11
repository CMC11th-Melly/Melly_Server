package cmc.mellyserver.domain.memory.query.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemoryDetailResponseDto implements Serializable {

	// ==== place =====
	private Long placeId;

	private String placeName;

	// ==== memory ====

	private Long memoryId;

	private String title;

	private String content;

	private List<ImageDto> memoryImages;

	private List<KeywordDto> keyword;

	private LocalDate visitedDate;

	private Long stars;

	// ==== group ====

	private Long groupId;

	private GroupType groupType;

	private String groupName;

	private int groupIcon;

	public MemoryDetailResponseDto(Long placeId, String placeName, Long memoryId, String title, String content,
		Long stars, LocalDate visitedDate, Long groupId, GroupType groupType, String groupName, int groupIcon) {

		this.placeId = placeId;
		this.placeName = placeName;
		this.memoryId = memoryId;
		this.title = title;
		this.content = content;
		this.stars = stars;
		this.visitedDate = visitedDate;
		this.groupId = groupId;
		this.groupType = groupType;
		this.groupName = groupName;
		this.groupIcon = groupIcon;
	}

}
