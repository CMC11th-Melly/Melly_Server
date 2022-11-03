package cmc.mellyserver.common.dataload;

import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.comment.application.CommentService;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
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
import cmc.mellyserver.notification.application.NotificationService;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.NotificationRepository;
import cmc.mellyserver.notification.domain.NotificationType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.placeScrap.application.PlaceScrapService;
import cmc.mellyserver.placeScrap.presentation.dto.ScrapRequest;
import cmc.mellyserver.user.domain.*;
import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import cmc.mellyserver.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PlaceRepository placeRepository;
    private final MemoryRepository memoryRepository;
    private final MemoryDomainService memoryDomainService;
    private final CommentService commentService;
    private final PlaceScrapService placeScrapService;
    private final NotificationService notificationService;

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

        User user2 = User.builder().uid("testuser")
                .email("test@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .profileImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/cdc6a8f9-8798-4214-94ae-1e5538944f60.jpg")
                .nickname("소피아")
                .ageGroup(AgeGroup.THREE)
                .provider(Provider.DEFAULT)
                .gender(Gender.MALE)
                .build();
        User saveUser2 = userRepository.save(user2);

        User user3 = User.builder().uid("testuser2")
                .email("test2@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .profileImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/cdc6a8f9-8798-4214-94ae-1e5538944f60.jpg")
                .nickname("레이")
                .ageGroup(AgeGroup.THREE)
                .provider(Provider.DEFAULT)
                .gender(Gender.MALE)
                .build();
        User saveUser3 = userRepository.save(user3);



        // 지금 사용자가 속해있는 그룹 1개 추가
        UserGroup userGroup = UserGroup.builder()
                                        .groupName("떡마방")
                                        .groupType(GroupType.FRIEND)
                                        .inviteLink("http://cmc11th.co.kr")
                                        .build();

        UserGroup userGroup2 = UserGroup.builder()
                .groupName("여자친구랑")
                .groupType(GroupType.COUPLE)
                .inviteLink("http://cmc11th.co.kr")
                .build();

        UserGroup userGroup3 = UserGroup.builder()
                .groupName("우리 가족")
                .groupType(GroupType.FAMILY)
                .inviteLink("http://cmc11th.co.kr")
                .build();

        UserGroup userGroup4 = UserGroup.builder()
                .groupName("학교 친구")
                .groupType(GroupType.FRIEND)
                .inviteLink("http://cmc11th.co.kr")
                .build();
//
        // 나랑 같은 그룹에 들어있는 사람이 나랑 같은 장소에 메모리를 쓴다. 하지만 이 사람은 다른 그룹에만 공개를 한데


        /**
         * 첫번째 그룹에는 유저 1과 유저 2가 포함되어 있음
         */
        GroupAndUser groupAndUser = new GroupAndUser();
        groupAndUser.setUser(saveUser);
        userGroup.setGroupUser(groupAndUser);
        GroupAndUser groupAndUser1 = new GroupAndUser();
        groupAndUser1.setUser(saveUser2);
        userGroup.setGroupUser(groupAndUser1);
        groupRepository.save(userGroup);

        /**
         * 두번째 그룹에는 유저 1과 유저 2가 포함되어 있다.
         */
        GroupAndUser groupAndUser2 = new GroupAndUser();
        GroupAndUser groupAndUser2_1 = new GroupAndUser();
        groupAndUser2.setUser(saveUser);
        groupAndUser2_1.setUser(saveUser2);
        userGroup2.setGroupUser(groupAndUser2);
        userGroup2.setGroupUser(groupAndUser2_1);
        groupRepository.save(userGroup2);

        /**
         * 세번째 그룹에는 유저 1과 유저 3이 포함되어 있다.
         */
        GroupAndUser groupAndUser3 = new GroupAndUser();
        GroupAndUser groupAndUser3_1 = new GroupAndUser();
        groupAndUser3.setUser(saveUser);
        groupAndUser3_1.setUser(saveUser3);
        userGroup3.setGroupUser(groupAndUser3);
        userGroup3.setGroupUser(groupAndUser3_1);
        groupRepository.save(userGroup3);

        /**
         *  네번째 그룹에는 유저 2,3이 포함되어 있다.
         */
        GroupAndUser groupAndUser4 = new GroupAndUser();
        groupAndUser4.setUser(user2);
        GroupAndUser groupAndUser5 = new GroupAndUser();
        groupAndUser5.setUser(user3);
        userGroup4.setGroupUser(groupAndUser4);
        userGroup4.setGroupUser(groupAndUser5);
        groupRepository.save(userGroup4);

        Place place1 = Place.builder().position(new Position(37.503837, 127.041793)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0888.JPG.jpg").placeName("무명요리사").placeCategory("일식").build();
        // memory 1
        Memory memory1 = Memory.builder().stars(5L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회").visitedDate(LocalDateTime.now()).content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory1.setUser(user);
        memory1.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save = memoryRepository.save(memory1);
        save.setPlaceForMemory(place1);

        // memory 2
        Memory memory1_2 = Memory.builder().stars(5L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회1").visitedDate(LocalDateTime.now()).content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory1_2.setUser(user);
        memory1_2.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save_2 = memoryRepository.save(memory1_2);
        save_2.setPlaceForMemory(place1);

        // memory 3
        Memory memory1_3 = Memory.builder().stars(5L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("제주도보다 맛있는 고등어회2").visitedDate(LocalDateTime.now()).content("친구 추천 받아서 갔는데 정말 맛있었다. 가게는 작고 아담한데 4명 이하로 오면 딱 좋을 느낌! 다음에 또 가장!").build();
        memory1_3.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory1_3.setUser(user);
        memory1_3.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save_3 = memoryRepository.save(memory1_3);
        save_3.setPlaceForMemory(place1);
        placeRepository.save(place1);

        Place place2 = Place.builder().position(new Position(37.511623, 127.023547)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/20210710%EF%BC%BF122523.jpg").placeName("쿠오레 에스프레소").placeCategory("카페, 디저트").build();

        // memory 1
        Memory memory2 = Memory.builder().stars(4L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory2.setUser(user);
        memory2.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save1_1 = memoryRepository.save(memory2);
        save1_1.setPlaceForMemory(place2);

        Memory memory2_1 = Memory.builder().stars(4L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2_1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory2_1.setUser(user);
        memory2_1.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save1_2 = memoryRepository.save(memory2_1);
        save1_2.setPlaceForMemory(place2);

        Memory memory2_2 = Memory.builder().stars(4L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("거의 반년만에 만나는 느낌..!! 오늘 너무 즐거웠어욤 담엔 또 언제 볼 수 있으려나ㅠㅠ 좋은 곳 데려가준 강남잘알 oo이 너무 고마워^-^").build();
        memory2_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory2_2.setUser(user);
        memory2_2.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save1_3 = memoryRepository.save(memory2_2);
        save1_3.setPlaceForMemory(place2);
        placeRepository.save(place2);


        Place place3 = Place.builder().position(new Position(37.511500, 127.023200)).placeImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(1).png").placeName("파인포레스트").placeCategory("글램핑").build();

        // memory 1
        Memory memory3 = Memory.builder().stars(3L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png")));
        memory3.setUser(user);
        memory3.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save2 = memoryRepository.save(memory3);
        save2.setPlaceForMemory(place3);

        Memory memory3_1 = Memory.builder().stars(3L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3_1.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg")));
        memory3_1.setUser(user);
        memory3_1.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save2_1 = memoryRepository.save(memory3_1);
        save2_1.setPlaceForMemory(place3);

        Memory memory3_2 = Memory.builder().stars(3L).groupInfo(new GroupInfo(userGroup.getGroupName(),GroupType.FRIEND,userGroup.getId())).openType(OpenType.ALL).title("연구실 탈출은 즐거워").visitedDate(LocalDateTime.now()).content("글램핑 처음이었는데 너무 춥지만 재미있었다ㅋㅅㅋ 교수님이 주신 꿀같은 휴가를 그냥 날릴 수 없지.. 모두 수고 많았어용 짱짱").build();
        memory3_2.setMemoryImages(List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png")));
        memory3_2.setUser(user);
        memory3_2.setKeyword(List.of("즐거워요","그냥 그래요"));
        Memory save2_2 = memoryRepository.save(memory3_2);
        save2_2.setPlaceForMemory(place3);
        placeRepository.save(place3);


        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(1),
            List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"),
                LocalDateTime.now().minusHours(1),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"),
                LocalDateTime.now().minusHours(1),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(1),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(1),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(2),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(2),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(2),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(2),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.ALL,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup2.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","다음에 또 올래요~",4L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        Memory memory = memoryDomainService.createMemory(saveUser.getUserId(),
                37.5000541000002, 127.02425909999957,
                "최고의 하루가 된 날!", "용용선생", "주류", "최고의 하루였다. 왜냐하면 오빠랑 재미있는 F1 경기를 보러왔기 때문이다. 만약 오늘 오지 않았다면 후회했겠지? 너무 즐겁다 푸하항~~~~끼하하하😂", 3L,
                1L, OpenType.ALL,
                List.of("즐거워요", "그냥 그래요", "행복해요", "기뻐요", "좋아요"), LocalDateTime.now(),
                List.of());

        memory.setMemoryImages(   List.of(new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled+(2).png"),
                new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/IMG_0887.JPG.jpg"),
                new MemoryImage("https://mellyimage.s3.ap-northeast-2.amazonaws.com/user1/Untitled.png"))
                );


        // 가장 최신 데이터

//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","다음에 또 올래요~",4L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요","행복해요","기뻐요","좋아요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.GROUP,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "그룹 없는 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                null,OpenType.PRIVATE,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "그룹 없는 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                null,OpenType.PRIVATE,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "그룹 없는 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                null,OpenType.PRIVATE,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "안녕하세요!","용용선생","주류","맛있게 먹었어용",3L,
//                null,OpenType.PRIVATE,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "안녕하세요!","용용선생","주류","맛있게 먹었어용",3L,
//                null,OpenType.PRIVATE,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.ALL,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());
//        memoryDomainService.createMemory(saveUser2.getUserId(),
//                37.5000541000002,127.02425909999957,
//                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
//                1L,OpenType.ALL,
//                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
//                List.of());

        /**
         *  첫번째 그룹에 속해있는 유저 2가 첫번째 그룹을 메모리에 설정 한 후 전체 공개로 올림
         */
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "첫번째 그룹의 두번째 유저가 씀~","용용선생","주류","맛있게 먹었어용",3L,
                1L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        /**
         * 나랑 같은 그룹에 속해있는 유저 2가 전체 공개로 올림
         */
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "첫번째 그룹에 들어있는 두번째 유저가 전체공개로 씀","용용선생","주류","맛있게 먹었어용",3L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        /**
         * 나랑 같은 그룹에 속해있는 유저 2가 전체 공개로 올림
         */
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "안녕하세요!","용용선생","주류","맛있게 먹었어용",3L,
                null,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());


        /**
         * 나랑 같은 그룹에 속해있는 유저 3이 3번째 그룹을 대상으로 전체 공개 글 씀
         */
        memoryDomainService.createMemory(saveUser3.getUserId(),
                37.5000541000002,127.02425909999957,
                "3번째 그룹의 3번째 유저가 씀~","용용선생","주류","진짜 성공...?",3L,
                3L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        // 나옴
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "2번째 그룹의 2번째 유저가 씀~","용용선생","주류","진짜 성공...?",3L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());


        // 나오면 안됨
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "4번째 그룹의 2번째 유저가 씀~","용용선생","주류","이거는 나오면 안됨! 나랑 같은 그룹이지만 공유는 다른 그룹과 한거임",3L,
                4L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());


        // 나오면 안됨
        memoryDomainService.createMemory(saveUser2.getUserId(),
                37.5000541000002,127.02425909999957,
                "1번째 그룹의 2번 유저가 비공개로 씀!","용용선생","주류","비공개 임으로 나오면 안됨",3L,
                1L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser3.getUserId(),
                37.5000541000002,127.02425909999957,
                "4번째 그룹의 3번 유저가 그룹공개로 씀!","용용선생","주류","내가 속해있지 않은 그룹을 대상으로 메모리 작성",3L,
                4L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser3.getUserId(),
                37.5000541000002,127.02425909999957,
                "3번째 그룹의 3번 유저가 그룹공개로 씀!","용용선생","주류","3번 그룹은 1번 유저와 같은 그룹임으로 나와야함",3L,
                3L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser3.getUserId(),
                37.5000541000002,127.02425909999957,
                "4번째 그룹의 3번째 유저가 전체공개로 씀!","용용선생","주류","이거는 나오면 안됨! 나랑 같은 그룹이지만 공유는 다른 그룹과 한거임",3L,
                4L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser3.getUserId(),
                37.5000541000002,127.02425909999957,
                "4번째 그룹의 3번째 유저가 비공개로 씀!","용용선생","주류","이거는 나오면 안됨! 나랑 같은 그룹이지만 공유는 다른 그룹과 한거임",3L,
                4L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

     // ======================================================================













// 여기부터는 다른 동네
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.49992030000013,127.02461119999951,
                "안녕하세요!","을지다락","주류","다음에 또 올래요~",4L,
                2L,OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.ALL,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.GROUP,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
//
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());
        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,

                List.of("즐거워요","그냥 그래요"), LocalDateTime.now().minusHours(1),
                List.of());

        memoryDomainService.createMemory(saveUser.getUserId(),
                37.503861,127.024144,
                "안녕하세요!","스타벅스","카페, 디저트","다음에 또 올래요~",4L,
                userGroup3.getId(),OpenType.PRIVATE,
                List.of("즐거워요","그냥 그래요"), LocalDateTime.now(),
                List.of());


        // 댓글 더미 데이터
        commentService.saveComment("cmc11th",new CommentRequest("ㅋㅋ 아 웃겨 실화냐?,,,, 가짜 데이터 만드는일 개노잼 제가 지금 이걸 왜 하는거죠?",74L,null,null));
        commentService.saveComment(user2.getUserId(),new CommentRequest("근데 레이,,,댓글은 대체 왜 까먹었어요?ㅎ",74L,null,null));
        commentService.saveComment(user3.getUserId(),new CommentRequest("빡치게 하지 마세요ㅎ",74L,2L,user2.getUserSeq()));
        commentService.saveComment(user3.getUserId(),new CommentRequest("ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ웃기다,,,ㅎ.. 나는 왜 지금 여기서 이 데이터를 쓰고 있는지 정말 모르겠지만요,,",74L,2L,user2.getUserSeq()));


        // 스크랩 더미 데이터
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.5000541000002,127.02425909999957, ScrapType.FRIEND,"용용선생","카페, 디저트"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.49992030000013,127.02461119999951, ScrapType.FRIEND,"을지다락","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.503861,127.024144, ScrapType.FRIEND,"스타벅스","카페, 디저트"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.503837, 127.041793, ScrapType.FRIEND,"무명요리사","일식"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.511623, 127.023547, ScrapType.FAMILY,"쿠오레 에스프레소","카페, 디저트"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.511500, 127.023200, ScrapType.FAMILY,"파인포레스트","글램핑"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.548400,126.912600, ScrapType.FAMILY,"따로집","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.560754,127.039851, ScrapType.COMPANY,"버킷리스트","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.559384,127.040284, ScrapType.COMPANY,"장모족발","족발/보쌈집"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.560019,127.040981, ScrapType.COMPANY,"베스킨라빈스","카페, 디저트"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.560489,127.040181, ScrapType.COUPLE,"대동집","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.560662,127.040467, ScrapType.COUPLE,"곱","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.560872,127.040201, ScrapType.COUPLE,"생활맥주","주류"));
        placeScrapService.createScrap("cmc11th",new ScrapRequest(37.561702,127.040677, ScrapType.COUPLE,"유유커피","카페, 디저트"));


        // 알림 더미데이터
        notificationService.createNotification(NotificationType.COMMENT,"내 메모리에 새 댓글이 달렸어요! 확인해보세요","cmc11th",74L);
        notificationService.createNotification(NotificationType.REPORT,"메모리에 신고가 들어왔어요! 확인해보세요","cmc11th",74L);



    }


}
