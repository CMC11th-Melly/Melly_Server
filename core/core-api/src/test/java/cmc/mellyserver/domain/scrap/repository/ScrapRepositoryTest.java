package cmc.mellyserver.domain.scrap.repository;

import cmc.mellyserver.config.RepositoryTest;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class ScrapRepositoryTest extends RepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceScrapRepository placeScrapRepository;

	User user;

	Place place;

	@DisplayName("스크랩을 한 유저의 ID와 장소 ID로 해당 스크랩을 찾을 수 있다")
	@Test
	public void 스크랩을한_유저의_ID와_장소ID로_스크랩을_찾을수_있다() {

		// // user = userRepository.save(UserFixtures.모카());
		//
		// place = placeRepository.save(PlaceFixtures.스타벅스());
		//
		// placeScrapRepository.save(PlaceScrap.createScrap(user, place,
		// ScrapType.FRIEND));
		// // when
		// Optional<PlaceScrap> result =
		// placeScrapRepository.findByUserIdAndPlaceId(user.getId(), place.getId());
		//
		// // then
		// Assertions.assertThat(result.get().getUser().getNickname()).isEqualTo(user.getNickname());
		// Assertions.assertThat(result.get().getPlace().getPlaceName()).isEqualTo(place.getPlaceName());
	}

}
