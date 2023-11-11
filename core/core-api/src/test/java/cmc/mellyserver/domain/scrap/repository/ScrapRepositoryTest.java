package cmc.mellyserver.domain.scrap.repository;

import static cmc.mellyserver.fixtures.PlaceFixtures.*;
import static cmc.mellyserver.fixtures.UserFixtures.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.support.RepositoryTestSupport;

public class ScrapRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceScrapRepository placeScrapRepository;

	@DisplayName("스크랩을 한 유저의 ID와 장소 ID로 해당 스크랩을 찾을 수 있다")
	@Test
	public void 스크랩을한_유저의_ID와_장소ID로_스크랩을_찾을수_있다() {

		// Given
		User 모카 = userRepository.save(모카());
		Place 스타벅스 = placeRepository.save(스타벅스());

		// when
		placeScrapRepository.save(PlaceScrap.createScrap(모카, 스타벅스, ScrapType.FRIEND));

		// then
		boolean exist = placeScrapRepository.existsByUserIdAndPlaceId(모카.getId(), 스타벅스.getId());
		Assertions.assertThat(exist).isTrue();
	}

}
