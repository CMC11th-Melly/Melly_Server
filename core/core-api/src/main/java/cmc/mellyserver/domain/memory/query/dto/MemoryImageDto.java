package cmc.mellyserver.domain.memory.query.dto;

import java.io.Serializable;

public record MemoryImageDto(Long imageId, String imageUrl) implements Serializable {
}
