package cmc.mellyserver.auth.controller.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckDuplicateNicknameRequest {

	private String nickname;

	public CheckDuplicateNicknameRequest(String nickname) {
		this.nickname = nickname;
	}

}
