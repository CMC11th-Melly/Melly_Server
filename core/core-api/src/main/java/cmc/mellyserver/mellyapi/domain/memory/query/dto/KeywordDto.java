package cmc.mellyserver.mellyapi.domain.memory.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KeywordDto implements Serializable {

    private Long id;
    private String content;
}
