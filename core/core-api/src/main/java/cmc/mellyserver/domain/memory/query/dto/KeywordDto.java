package cmc.mellyserver.domain.memory.query.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeywordDto implements Serializable {

	private Long id;

	private String content;

}
