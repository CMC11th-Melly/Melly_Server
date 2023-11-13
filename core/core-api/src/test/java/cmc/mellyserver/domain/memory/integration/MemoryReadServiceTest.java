package cmc.mellyserver.domain.memory.integration;

import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.memory.MemoryService;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.support.IntegrationTestSupport;
import fixtures.GroupFixtures;
import fixtures.MemoryFixtures;
import fixtures.PlaceFixtures;

public class MemoryReadServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemoryService memoryService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemoryRepository memoryRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private GroupAndUserRepository groupAndUserRepository;

	@DisplayName("내가 작성한 메모리를 조회할때")
	@ParameterizedTest
	@CsvSource({
		"5,    false",
		"4,   true",
	})
	void 내가_작성한_메모리를_조회한다(int pageSize, boolean hasNext) {

		// given
		User 모카 = userRepository.save(모카());

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(1L, 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
		}

		// when
		MemoryListResponse result = memoryService.getUserMemories(-1L, PageRequest.of(0, pageSize), 모카.getId(),
			null,
			null);

		// then
		assertThat(result.getContents()).hasSize(pageSize);
		assertThat(result.getNext()).isEqualTo(hasNext);
	}

	@DisplayName("내가 작성한 메모리를 조회할때 커서값이 존재한다면2")
	@Test
	void 내가_작성한_메모리를_조회할때_커서값이_존재한다면2() {

		// given
		User 모카 = userRepository.save(모카());

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(1L, 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
		}

		List<Memory> all = memoryRepository.findAll();
		Collections.reverse(all);

		// when
		MemoryListResponse result = memoryService.getUserMemories(all.get(0).getId() - 2, PageRequest.of(0, 1),
			모카.getId(), null,
			null);

		// then
		assertThat(result.getContents()).hasSize(1);
		assertThat(result.getNext()).isEqualTo(true);
	}

	@DisplayName("특정 장소에 대해 내가 작성한 메모리를 조회한다2")
	@Test
	void 특정_장소에_대해_내가_작성한_메모리를_조회한다2() {

		// given
		User 모카 = userRepository.save(모카());
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
		}

		List<Memory> all = memoryRepository.findAll();
		Collections.reverse(all);

		// when
		MemoryListResponse result = memoryService.getUserMemoriesInPlace(all.get(0).getId() - 2,
			PageRequest.of(0, 1),
			모카.getId(),
			스타벅스.getId(), null);

		// then
		assertThat(result.getContents()).hasSize(1);
		assertThat(result.getNext()).isEqualTo(true);
	}

	@DisplayName("특정 장소에 대해 다른 사람이 작성한 메모리를 조회한다")
	@Test
	void 특정_장소에_대해_다른사람이_작성한_메모리를_조회한다() {

		// given
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
		}
		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 머식.getId(), null, "성수 재밌었다", OpenType.ALL));
		}

		// when
		MemoryListResponse result = memoryService.getOtherMemoriesInPlace(-1L,
			PageRequest.of(0, 10),
			모카.getId(),
			스타벅스.getId(), null);

		// then
		assertThat(result.getContents()).hasSize(5);
		assertThat(result.getNext()).isEqualTo(false);
	}

	@DisplayName("특정 장소에 대해 다른 사람이 작성한 메모리를 조회한다2")
	@Test
	void 특정_장소에_대해_다른사람이_작성한_메모리를_조회한다2() {

		// given
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "성수 재밌었다", OpenType.ALL));
		}
		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 머식.getId(), null, "성수 재밌었다", OpenType.ALL));
		}

		List<Memory> all = memoryRepository.findAll();
		Collections.reverse(all);

		// when
		MemoryListResponse result = memoryService.getOtherMemoriesInPlace(all.get(0).getId() - 2,
			PageRequest.of(0, 5),
			모카.getId(),
			스타벅스.getId(), null);

		// then
		assertThat(result.getContents()).hasSize(2);
		assertThat(result.getNext()).isEqualTo(false);
	}

	@DisplayName("특정 장소에 대해 같은 그룹의 사람이 작성한 메모리를 조회한다")
	@Test
	void 특정_장소에_대해_같은_그룹의_시림이_작성한_메모리를_조회한다() {

		// given
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		groupAndUserRepository.save(GroupAndUser.of(모카, 친구들));
		groupAndUserRepository.save(GroupAndUser.of(머식, 친구들));

		for (int i = 0; i < 5; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), 친구들.getId(), "성수 재밌었다", OpenType.ALL));
		}
		for (int i = 0; i < 3; i++) {
			memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 머식.getId(), 친구들.getId(), "성수 재밌었다", OpenType.ALL));
		}

		// when
		MemoryListResponse result = memoryService.getGroupMemoriesInPlace(-1L,
			PageRequest.of(0, 10), 모카.getId(), 스타벅스.getId(), null);

		// then
		assertThat(result.getContents()).hasSize(3);
		assertThat(result.getNext()).isEqualTo(false);
	}
}
