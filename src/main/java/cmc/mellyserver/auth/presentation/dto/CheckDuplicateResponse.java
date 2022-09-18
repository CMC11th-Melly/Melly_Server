package cmc.mellyserver.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CheckDuplicateResponse {
    private boolean isDuplicated;
}
