package cmc.mellyserver.auth.controller.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckDuplicateEmailRequest {

	private String email;

	public CheckDuplicateEmailRequest(String email) {
		this.email = email;
	}

}
