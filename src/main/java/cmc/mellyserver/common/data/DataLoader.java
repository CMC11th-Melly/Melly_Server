package cmc.mellyserver.common.data;

import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.OpenType;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final MemoryDomainService memoryDomainService;
    private final PlaceRepository placeRepository;
    private final MemoryRepository memoryRepository;

    // TODO : Builder 사용시 엔티티 초기화 되지 않는 문제
    @Transactional
    public void loadData()
    {
        userRepository.deleteAll();
        groupRepository.deleteAll();
        placeRepository.deleteAll();
        memoryRepository.deleteAll();

        // 테스트 유저 입력
        User user = User.builder().uid("cmc11th")
                .email("melly@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .profileImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/cdc6a8f9-8798-4214-94ae-1e5538944f60.jpg")
                .nickname("떡잎마을방범대")
                .ageGroup(AgeGroup.TWO)
                .provider(Provider.DEFAULT)
                .gender(Gender.MALE)
                .build();
        User saveUser = userRepository.save(user);

        // 지금 사용자가 속해있는 그룹 1개 추가
        UserGroup userGroup = UserGroup.builder()
                                        .groupName("CMC 11기 떡잎마을방범대")
                                        .groupType(GroupType.FRIEND)
                                        .inviteLink("http://cmc11th.co.kr")
                                        .build();

        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(saveUser);
        userGroup.setGroupUser(groupAndUser);
        groupRepository.save(userGroup);

        Place place1 = Place.builder().position(new Position(37.503837, 127.041793)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0888.JPG.jpg").name("무명요리사").build();
        Memory memory1 = Memory.builder().title("제주도보다 맛있는 고등어회").content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1.setMemoryImages(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg"));
        Memory save = memoryRepository.save(memory1);
        save.setPlaceForMemory(place1);
        placeRepository.save(place1);

        Place place2 = Place.builder().position(new Position(37.511623, 127.023547)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/20210710%EF%BC%BF122523.jpg").name("쿠오레 에스프레소").build();
        Memory memory2 = Memory.builder().title("오랜만에 너무 즐거웠어욤").content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2.setMemoryImages(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png"));
        Memory save1 = memoryRepository.save(memory2);
        save1.setPlaceForMemory(place2);
        placeRepository.save(place2);

        Place place3 = Place.builder().position(new Position(37.511623, 127.023547)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(1).png").name("파인포레스트").build();
        Memory memory3 = Memory.builder().title("연구실 탈출은 즐거워").content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3.setMemoryImages(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png"));
        Memory save2 = memoryRepository.save(memory3);
        save2.setPlaceForMemory(place3);
        placeRepository.save(place3);


        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FRIEND,
                "다음에 또 올래요~",
                4,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FRIEND,
                "다음에 또 올래요~",
                3,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FRIEND,
                "다음에 또 올래요~",
                2,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "을지다락",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");


        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "용용선생",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "주류");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FAMILY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.FRIEND,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COUPLE,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "교보문고",GroupType.COMPANY,
                "다음에 또 올래요~",
                5,
                OpenType.ALL,
                1L,
                "서점");

    }
}
