package cmc.mellyserver.domain.scrap.unit;

import static fixtures.UserFixtures.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.dbcore.user.User;

public class ScrapTest {

	@DisplayName("스크랩을 생성한다")
	@Test
	void 스크랩을_생성한다() {

		// given
		User savedUser = 모카();
		Place savedPlace = Place.builder().build();

		// when
		PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.COMPANY);

		// then
		Assertions.assertThat(scrap.getUser()).usingRecursiveComparison().isEqualTo(savedUser);
	}

}
