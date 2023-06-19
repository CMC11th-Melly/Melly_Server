package cmc.mellyserver.mellyappexternalapi.common.factory;

import cmc.mellyserver.mellydomain.group.domain.GroupAndUser;

public class GroupAndUserFactory {

	public static GroupAndUser mockGroupAndUser() {
		return GroupAndUser.builder()
			.user(null)
			.group(null)
			.build();
	}
}
