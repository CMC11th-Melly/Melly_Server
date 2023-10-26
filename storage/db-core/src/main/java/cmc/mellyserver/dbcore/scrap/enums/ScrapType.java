package cmc.mellyserver.dbcore.scrap.enums;

import java.util.Objects;

public enum ScrapType {

	FRIEND, FAMILY, COUPLE, COMPANY;

	public static ScrapType from(final String scrapType) {
		if (Objects.isNull(scrapType)) {
			return null;
		}

		try {
			return ScrapType.valueOf(scrapType.toUpperCase());
		}
		catch (final IllegalArgumentException e) {
			throw new IllegalArgumentException();
		}
	}

}
