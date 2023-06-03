package cmc.mellyserver.mellyapi.common.factory;

import cmc.mellyserver.mellycore.group.domain.GroupAndUser;

public class GroupAndUserFactory {

	public static GroupAndUser mockGroupAndUser() {
		return GroupAndUser.builder()
			.user(null)
			.group(null)
			.build();
	}
}
