package cmc.mellyserver.mellyappexternalapi.memory.application.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.mellydomain.common.enums.OpenType;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateMemoryRequestDto {

	Long userSeq;
	Double lat;
	Double lng;
	String title;
	String placeName;
	String placeCategory;
	String content;
	Long star;
	Long groupId;
	OpenType openType;
	List<String> keyword;
	LocalDateTime visitedDate;
	List<MultipartFile> multipartFiles;

	@Builder
	public CreateMemoryRequestDto(Long userSeq, Double lat, Double lng, String title, String placeName,
		String placeCategory, String content, Long star, Long groupId, OpenType openType, List<String> keyword,
		LocalDateTime visitedDate, List<MultipartFile> multipartFiles) {
		this.userSeq = userSeq;
		this.lat = lat;
		this.lng = lng;
		this.title = title;
		this.placeName = placeName;
		this.placeCategory = placeCategory;
		this.content = content;
		this.star = star;
		this.groupId = groupId;
		this.openType = openType;
		this.keyword = keyword;
		this.visitedDate = visitedDate;
		this.multipartFiles = multipartFiles;
	}
}
