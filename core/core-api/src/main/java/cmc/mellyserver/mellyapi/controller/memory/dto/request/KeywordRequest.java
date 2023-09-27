package cmc.mellyserver.mellyapi.controller.memory.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class KeywordRequest {
    private List<String> keyword;
}
