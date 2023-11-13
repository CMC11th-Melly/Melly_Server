package cmc.mellyserver.domain.place.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import cmc.mellyserver.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.support.RepositoryTestSupport;
import fixtures.MemoryFixtures;
import fixtures.PlaceFixtures;
import fixtures.UserFixtures;

@Transactional
public class PlaceQueryRepositoryTest extends RepositoryTestSupport {

	@Autowired
	private PlaceQueryRepository placeQueryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private MemoryRepository memoryRepository;

	@DisplayName("사용자가 작성한 메모리가 존재하는 장소 리스트를 받아온다")
	@Test
	void 사용자가_작성한_메모리가_존재하는_장소_리스트를_조회한다() {

		// given
		User 모카 = userRepository.save(UserFixtures.모카());
		User 머식 = userRepository.save(UserFixtures.머식());

		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
		Place 이디야 = placeRepository.save(PlaceFixtures.이디야());

		memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "스타벅스 방문!", OpenType.ALL));
		memoryRepository.save(MemoryFixtures.메모리(이디야.getId(), 머식.getId(), null, "이디야 방문!", OpenType.ALL));

		// when
		List<Place> places = placeQueryRepository.getPlaceUserMemoryExist(모카.getId(), null);

		// then
		assertThat(places).hasSize(1);
	}

	@DisplayName("메모리 제목으로 메모리가 속한 장소를 조회한다")
	@Test
	void 메모리_제목으로_메모리가_속한_장소를_조회한다() {

		// given
		User 모카 = userRepository.save(UserFixtures.모카());
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());

		memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "스타벅스 방문!", OpenType.ALL));

		// when
		List<FindPlaceByMemoryTitleResponseDto> places = placeQueryRepository.searchPlaceByContainMemoryName(
			모카.getId(), "스타벅");

		// then
		Assertions.assertThat(places).hasSize(1);
	}

}
