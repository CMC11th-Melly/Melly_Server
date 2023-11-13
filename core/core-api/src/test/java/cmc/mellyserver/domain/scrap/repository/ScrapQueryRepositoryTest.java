package cmc.mellyserver.domain.scrap.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import cmc.mellyserver.support.RepositoryTestSupport;

public class ScrapQueryRepositoryTest extends RepositoryTestSupport {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private PlaceScrapRepository placeScrapRepository;

  @Autowired
  private PlaceScrapQueryRepository placeScrapQueryRepository;

  @DisplayName("로그인한 유저가 현재 장소를 스크랩했는지를 장소 좌표 기준으로 체크한다.")
  @Test
  void check_login_user_scraped_by_position() {

	// given
	User user = User.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
	User savedUser = userRepository.save(user);

	Place place = Place.builder().position(new Position(1.234, 1.234)).name("테스트 장소").build();
	Place savedPlace = placeRepository.save(place);

	PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
	placeScrapRepository.save(scrap);

	// when
	Boolean isLoginUserScrapedPlace = placeScrapQueryRepository
		.checkCurrentLoginUserScrapedPlaceByPosition(savedUser.getId(), savedPlace.getPosition());

	// then
	Assertions.assertThat(isLoginUserScrapedPlace).isTrue();
  }

  @DisplayName("로그인한 유저가 현재 장소를 스크랩했는지를 장소 식별자를 기준으로 체크한다.")
  @Test
  void check_login_user_scraped_by_place_id() {

	// given
	User user = User.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
	User savedUser = userRepository.save(user);

	Place place = Place.builder().position(new Position(1.234, 1.234)).name("테스트 장소").build();
	Place savedPlace = placeRepository.save(place);

	PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
	placeScrapRepository.save(scrap);

	// when
	Boolean isLoginUserScrapedPlace = placeScrapQueryRepository
		.checkCurrentLoginUserScrapedPlaceByPlaceId(savedUser.getId(), savedPlace.getId());

	// then
	Assertions.assertThat(isLoginUserScrapedPlace).isTrue();
  }

}
