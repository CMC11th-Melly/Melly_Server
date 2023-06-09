package cmc.mellyserver.mellycore.unit.memory.repository;

import static cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures.재현;
import static cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures.제민;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public class MemoryQueryRepositoryTest extends RepositoryTest {

    @Autowired
    private MemoryQueryRepository memoryQueryRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupAndUserRepository groupAndUserRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;


    @DisplayName("장소에 포함된 메모리 제목으로 장소를 검색한다.")
    @Test
    void 장소에_포함된_메모리_제목으로_장소를_검색한다() {

        // given
        User 제민 = userRepository.save(제민());

        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        Memory savedMemory = memoryRepository.save(memory);

        // when
        List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos = memoryQueryRepository.searchPlaceByContainMemoryName(
                제민.getUserSeq(), savedMemory.getTitle());

        // then
        Assertions.assertThat(findPlaceInfoByMemoryNameResponseDtos).hasSize(1);
    }

    @DisplayName("특정 장소에 로그인한 사용자가 작성한 메모리를 조회한다.")
    @Test
    void 특정_장소에_로그인한_사용자가_작성한_메모리를_조회한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        Memory savedMemory = memoryRepository.save(memory);
        savedMemory.setKeyword(List.of("좋아요"));
        // when
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryUserCreatedForPlace(
                PageRequest.of(0, 10), 제민.getUserSeq(),
                savedPlace.getId(), GroupType.ALL);

        // then
        Assertions.assertThat(memoryResponseDtos.getContent()).hasSize(1);
    }

    @DisplayName("로그인한 사용자가 작성한 메모리를 조회한다.")
    @Test
    void 로그인한_사용자가_작성한_메모리를_모두_조회한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());
        User 재현 = userRepository.save(재현());

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        memory.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory);

        Memory memory2 = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(재현.getUserSeq()).build();
        memory2.setKeyword(List.of("좋아요"));
        Memory savedMemory2 = memoryRepository.save(memory2);

        // when
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryUserCreatedForMyPage(
                PageRequest.of(0, 10), 제민.getUserSeq(),
                GroupType.ALL);

        // then
        Assertions.assertThat(memoryResponseDtos.getContent()).hasSize(1);
    }

    @DisplayName("로그인하지 않은 사용자가 작성한 메모리를 조회한다.")
    @Test
    void 로그인하지_않은_사용자가_작성한_메모리를_모두_조회한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());
        User 재현 = userRepository.save(재현());

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        memory.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory);

        Memory memory2 = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(재현.getUserSeq()).build();
        memory2.setKeyword(List.of("좋아요"));
        Memory savedMemory2 = memoryRepository.save(memory2);

        // when
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryOtherCreate(
                PageRequest.of(0, 10), 재현.getUserSeq(),
                savedPlace.getId(), GroupType.ALL);

        // then
        Assertions.assertThat(memoryResponseDtos.getContent()).hasSize(1);
    }

    @DisplayName("내 그룹에 속한 사용자가 작성한 메모리를 조회한다.")
    @Test
    void 내그룹에_속한_사용자가_작성한_메모리를_모두_조회한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());
        User 재현 = userRepository.save(재현());

        UserGroup 동료_그룹 = UserGroup.builder().groupName("동료")
                .groupType(GroupType.COMPANY).build();

        groupAndUserRepository.saveAll(
                List.of(new GroupAndUser(제민, 동료_그룹), new GroupAndUser(재현, 동료_그룹)));
        UserGroup userGroup = groupRepository.save(동료_그룹);

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).groupInfo(
                        new GroupInfo(userGroup.getGroupName(), userGroup.getGroupType(),
                                userGroup.getId())).build();
        memory.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory);

        Memory memory2 = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(재현.getUserSeq()).groupInfo(
                        new GroupInfo(userGroup.getGroupName(), userGroup.getGroupType(),
                                userGroup.getId())).build();
        memory2.setKeyword(List.of("좋아요"));
        Memory savedMemory2 = memoryRepository.save(memory2);

        // when
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.getMyGroupMemory(
                PageRequest.of(0, 10), 재현.getUserSeq(),
                savedPlace.getId(), GroupType.ALL);

        // then
        Assertions.assertThat(memoryResponseDtos.getContent()).hasSize(0);
    }

    @DisplayName("특정 장소에 내가 작성한 메모리 개수를 카운트한다.")
    @Test
    void 특정_장소에_내가_작성한_메모리_개수를_카운트한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());
        User 재현 = userRepository.save(재현());

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        memory.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory);

        Memory memory2 = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(재현.getUserSeq()).build();
        memory2.setKeyword(List.of("좋아요"));
        Memory savedMemory2 = memoryRepository.save(memory2);

        // when
        HashMap<String, Long> map = memoryQueryRepository.countMemoriesBelongToPlace(
                제민.getUserSeq(), savedPlace.getId());

        // then
        Assertions.assertThat(map.get("belongToUser")).isEqualTo(1L);
        Assertions.assertThat(map.get("notBelongToUser")).isEqualTo(1L);
    }


    @DisplayName("메모리 식별자를 기반으로 메모리 상세 정보를 조회한다.")
    @Test
    void 메모리_식별자를_기반으로_메모리_상세_정보를_조회한다() {

        // given
        Place place = Place.builder().placeName("테스트 장소")
                .position(new Position(1.234, 1.234)).build();
        Place savedPlace = placeRepository.save(place);

        User 제민 = userRepository.save(제민());

        Memory memory = Memory.builder().title("테스트 제목").placeId(savedPlace.getId())
                .userId(제민.getUserSeq()).build();
        memory.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory);

        // when
        MemoryResponseDto memoryByMemoryId = memoryQueryRepository.getMemoryByMemoryId(
                제민.getUserSeq(), savedMemory.getId());

        // then
        Assertions.assertThat(memoryByMemoryId.getPlaceId()).isEqualTo(savedPlace.getId());
    }
}
