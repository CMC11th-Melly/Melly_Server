package cmc.mellyserver.mellyappexternalapi.healthcheck.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PwEncode {
	private String pass;
}
