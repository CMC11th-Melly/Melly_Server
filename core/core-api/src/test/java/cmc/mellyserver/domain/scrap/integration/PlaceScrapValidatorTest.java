package cmc.mellyserver.domain.scrap.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.scrap.PlaceScrapValidator;
import cmc.mellyserver.fixtures.PlaceFixtures;
import cmc.mellyserver.fixtures.UserFixtures;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;

public class PlaceScrapValidatorTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceScrapRepository placeScrapRepository;

	@Autowired
	private PlaceScrapValidator placeScrapValidator;

	@DisplayName("이미 스크랩을 한 상태라면 중복 예외가 발생한다")
	@Test
	void 이미_스크랩을_한_상태라면_예외가_발생한다() {

		// given
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
		User 모카 = userRepository.save(UserFixtures.모카());

		// when
		placeScrapRepository.save(PlaceScrap.createScrap(모카, 스타벅스, ScrapType.FRIEND));

		// then
		assertThatThrownBy(() -> placeScrapValidator.validateDuplicatedScrap(모카.getId(), 스타벅스.getId()))
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.DUPLICATE_SCRAP.getMessage());
	}

	@DisplayName("기존에 스크랩이 존재하지 않는다면 삭제하지 못하고 예외가 발생한다.")
	@Test
	void 스크랩이_존재하지않는다면_삭제_불가능하다() {

		// given
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
		User 모카 = userRepository.save(UserFixtures.모카());

		// when & then
		assertThatThrownBy(() -> placeScrapValidator.validateExistedScrap(모카.getId(), 스타벅스.getId()))
			.isInstanceOf(BusinessException.class)
			.hasMessage(ErrorCode.NOT_EXIST_SCRAP.getMessage());
	}
}
