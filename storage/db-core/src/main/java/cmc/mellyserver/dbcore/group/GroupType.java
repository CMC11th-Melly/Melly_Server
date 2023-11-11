package cmc.mellyserver.dbcore.group.enums;

import java.util.Objects;

public enum GroupType {

	FAMILY, COMPANY, COUPLE, FRIEND;

	public static GroupType from(final String groupType) {
		if (Objects.isNull(groupType)) {
			return null;
		}

		try {
			return GroupType.valueOf(groupType.toUpperCase());
		}
		catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}
	}

}
