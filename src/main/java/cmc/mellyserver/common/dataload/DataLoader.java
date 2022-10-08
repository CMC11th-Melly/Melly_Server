package cmc.mellyserver.common.dataload;

import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.domain.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.domain.service.MemoryDomainService;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.*;
import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import cmc.mellyserver.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PlaceRepository placeRepository;
    private final MemoryRepository memoryRepository;
    private final MemoryDomainService memoryDomainService;

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

        UserGroup userGroup2 = UserGroup.builder()
                .groupName("정시템 친구들")
                .groupType(GroupType.FRIEND)
                .inviteLink("http://cmc11th.co.kr")
                .build();

        UserGroup userGroup3 = UserGroup.builder()
                .groupName("우리 가족")
                .groupType(GroupType.FAMILY)
                .inviteLink("http://cmc11th.co.kr")
                .build();

        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(saveUser);
        userGroup.setGroupUser(groupAndUser);
        groupRepository.save(userGroup);

        GroupAndUser groupAndUser2 = new GroupAndUser();
        groupAndUser2.setUser(saveUser);
        userGroup2.setGroupUser(groupAndUser2);
        groupRepository.save(userGroup2);

        GroupAndUser groupAndUser3 = new GroupAndUser();
        groupAndUser3.setUser(saveUser);
        userGroup3.setGroupUser(groupAndUser3);
        groupRepository.save(userGroup3);

        Place place1 = Place.builder().position(new Position(37.503837, 127.041793)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0888.JPG.jpg").placeName("무명요리사").placeCategory("일식").build();
        // memory 1
        Memory memory1 = Memory.builder().stars(5L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회").content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory1.setUser(user);
        memory1.setKeyword(List.of("최고에요","맛있어요"));
        Memory save = memoryRepository.save(memory1);
        save.setPlaceForMemory(place1);

        // memory 2
        Memory memory1_2 = Memory.builder().stars(5L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회1").content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory1_2.setUser(user);
        memory1_2.setKeyword(List.of("최고에요","맛있어요"));
        Memory save_2 = memoryRepository.save(memory1_2);
        save_2.setPlaceForMemory(place1);

        // memory 3
        Memory memory1_3 = Memory.builder().stars(5L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회2").content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1_3.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory1_3.setUser(user);
        memory1_3.setKeyword(List.of("최고에요","맛있어요"));
        Memory save_3 = memoryRepository.save(memory1_3);
        save_3.setPlaceForMemory(place1);
        placeRepository.save(place1);

        Place place2 = Place.builder().position(new Position(37.511623, 127.023547)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/20210710%EF%BC%BF122523.jpg").placeName("쿠오레 에스프레소").placeCategory("카페, 디저트").build();

        // memory 1
        Memory memory2 = Memory.builder().stars(4L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory2.setUser(user);
        memory2.setKeyword(List.of("행복해요","좋아요"));
        Memory save1_1 = memoryRepository.save(memory2);
        save1_1.setPlaceForMemory(place2);

        Memory memory2_1 = Memory.builder().stars(4L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2_1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory2_1.setUser(user);
        memory2_1.setKeyword(List.of("행복해요","좋아요"));
        Memory save1_2 = memoryRepository.save(memory2_1);
        save1_2.setPlaceForMemory(place2);

        Memory memory2_2 = Memory.builder().stars(4L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory2_2.setUser(user);
        memory2_2.setKeyword(List.of("행복해요","좋아요"));
        Memory save1_3 = memoryRepository.save(memory2_2);
        save1_3.setPlaceForMemory(place2);
        placeRepository.save(place2);


        Place place3 = Place.builder().position(new Position(37.511623, 127.023547)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(1).png").placeName("파인포레스트").placeCategory("글램핑").build();

        // memory 1
        Memory memory3 = Memory.builder().stars(3L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory3.setUser(user);
        memory3.setKeyword(List.of("기뻐요","슬퍼요"));
        Memory save2 = memoryRepository.save(memory3);
        save2.setPlaceForMemory(place3);

        Memory memory3_1 = Memory.builder().stars(3L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3_1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory3_1.setUser(user);
        memory3_1.setKeyword(List.of("기뻐요","슬퍼요"));
        Memory save2_1 = memoryRepository.save(memory3_1);
        save2_1.setPlaceForMemory(place3);

        Memory memory3_2 = Memory.builder().stars(3L).groupInfo(new GroupInfo(GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory3_2.setUser(user);
        memory3_2.setKeyword(List.of("기뻐요","슬퍼요"));
        Memory save2_2 = memoryRepository.save(memory3_2);
        save2_2.setPlaceForMemory(place3);
        placeRepository.save(place3);

        /*
        위도
        경도
        장소사진은 굳이..? 장소 사진 저장은 지금은 끄기
        장소 이름
        groupId
        group카테고리 -> 편한 필터링 위해서...!
        공개 타입 (group 설정했으면 default로 OPENTYPE.GROUP, 만약 전체 공개 눌렀으면 OPENTYPE.ALL 사용!)
        메모리 제목
        메모리 내용
        별점
        키워드들
        메모리 사진들
         */
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FAMILY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.FRIEND,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COUPLE,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                1L,
                GroupType.COMPANY,
                List.of("최고에요","기뻐요"),
                List.of());

    }
}
