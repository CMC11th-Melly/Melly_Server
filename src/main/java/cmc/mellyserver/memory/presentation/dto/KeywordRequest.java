package cmc.mellyserver.memory.presentation.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.ProtectionDomain;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class KeywordRequest {
    private List<String> keyword;
}
