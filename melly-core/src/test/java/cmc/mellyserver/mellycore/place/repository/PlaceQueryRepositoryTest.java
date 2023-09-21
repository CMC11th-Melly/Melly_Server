package cmc.mellyserver.mellycore.place.repository;

import cmc.mellyserver.mellycore.config.RepositoryTest;

public class PlaceQueryRepositoryTest extends RepositoryTest {


//    @Autowired
//    private PlaceQueryRepository placeQueryRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PlaceRepository placeRepository;
//
//    @Autowired
//    private MemoryRepository memoryRepository;
//
//    @Autowired
//    private PlaceScrapRepository placeScrapRepository;
//    ;
//
//
//    @DisplayName("사용자가 작성한 메모리가 존재하는 장소 리스트를 받아온다.")
//    @Test
//    void get_place_login_user_made_memory() {
//
//        // given
//        User user = User.cmc.mellyserver.mellycore.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
//        User otherUser = User.cmc.mellyserver.mellycore.builder().nickname("테스트 유저2").provider(Provider.DEFAULT).build();
//        User savedUser = userRepository.save(user);
//        User savedOtherUser = userRepository.save(otherUser);
//
//        Place place = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.234, 1.234)).placeName("테스트 장소")
//                .build();
//        Place savedPlace = placeRepository.save(place);
//
//        Place place2 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.235, 1.235)).placeName("테스트 장소2")
//                .build();
//        Place savedPlace2 = placeRepository.save(place2);
//
//        Memory memory = Memory.cmc.mellyserver.mellycore.builder().userId(savedUser.getId()).placeId(savedPlace.getId())
//                .title("테스트 메모리").build();
//
//        Memory savedMemory = memoryRepository.save(memory);
//
//        Memory memory2 = Memory.cmc.mellyserver.mellycore.builder().userId(savedOtherUser.getId())
//                .placeId(savedPlace2.getId())
//                .title("테스트 메모리2").build();
//
//        Memory savedMemory2 = memoryRepository.save(memory2);
//
//        // when
//        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(
//                user.getId());
//
//        // then
//        Assertions.assertThat(placeUserMemoryExist).hasSize(1);
//        Assertions.assertThat(placeUserMemoryExist)
//                .extracting("placeName")
//                .containsExactlyInAnyOrder("테스트 장소");
//    }
//
//    @DisplayName("로그인한 사용자가 스크랩한 장소 개수를 타입별로 조회할 수 있다.")
//    @Test
//    void get_place_count_login_user_write() {
//
//        // given
//        User user = User.cmc.mellyserver.mellycore.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
//        User savedUser = userRepository.save(user);
//
//        Place place = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.234, 1.234)).placeName("테스트 장소")
//                .build();
//        Place savedPlace = placeRepository.save(place);
//
//        Place place2 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.235, 1.235)).placeName("테스트 장소2")
//                .build();
//        Place savedPlace2 = placeRepository.save(place2);
//
//        Place place3 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.236, 1.236)).placeName("테스트 장소3")
//                .build();
//        Place savedPlace3 = placeRepository.save(place3);
//
//        Place place4 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.237, 1.237)).placeName("테스트 장소4")
//                .build();
//        Place savedPlace4 = placeRepository.save(place4);
//
//        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
//
//        PlaceScrap scrap1 = PlaceScrap.createScrap(savedUser, savedPlace2, ScrapType.FRIEND);
//
//        PlaceScrap scrap2 = PlaceScrap.createScrap(savedUser, savedPlace3, ScrapType.COMPANY);
//
//        PlaceScrap scrap3 = PlaceScrap.createScrap(savedUser, savedPlace4, ScrapType.COUPLE);
//
//        placeScrapRepository.saveAll(List.of(scrap, scrap1, scrap2, scrap3));
//
//        // when
//        List<PlaceScrapCountResponseDto> scrapedPlaceGrouping = placeQueryRepository.getScrapedPlaceGrouping(
//                savedUser);
//
//        // then
//        Assertions.assertThat(scrapedPlaceGrouping).hasSize(3);
//        Assertions.assertThat(scrapedPlaceGrouping).extracting("scrapCount")
//                .containsExactlyInAnyOrder(1L, 1L, 2L);
//    }
//
//    @DisplayName("스크랩한 장소 리스트를 스크랩 타입별로 가져온다.")
//    @ParameterizedTest
//    @CsvSource(value = {"FRIEND,2", "COMPANY,1", "COUPLE,1", "ALL,4"})
//    void get_scraped_place_list(ScrapType scrapType, int expected) {
//
//        // given
//        User user = User.cmc.mellyserver.mellycore.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
//        User otherUser = User.cmc.mellyserver.mellycore.builder().nickname("테스트 유저2").provider(Provider.DEFAULT).build();
//        User savedUser = userRepository.save(user);
//        User savedOtherUser = userRepository.save(otherUser);
//
//        Place place = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.234, 1.234)).placeName("테스트 장소")
//                .build();
//        Place savedPlace = placeRepository.save(place);
//
//        Place place2 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.235, 1.235)).placeName("테스트 장소2")
//                .build();
//        Place savedPlace2 = placeRepository.save(place2);
//
//        Place place3 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.236, 1.236)).placeName("테스트 장소3")
//                .build();
//        Place savedPlace3 = placeRepository.save(place3);
//
//        Place place4 = Place.cmc.mellyserver.mellycore.builder().position(new Position(1.237, 1.237)).placeName("테스트 장소4")
//                .build();
//        Place savedPlace4 = placeRepository.save(place4);
//
//        Memory memory = Memory.cmc.mellyserver.mellycore.builder().userId(savedUser.getId()).placeId(savedPlace.getId())
//                .title("테스트 메모리").build();
//
//        memoryRepository.save(memory);
//
//        Memory memory2 = Memory.cmc.mellyserver.mellycore.builder().userId(savedOtherUser.getId())
//                .placeId(savedPlace.getId())
//                .title("테스트 메모리").build();
//
//        memoryRepository.save(memory2);
//
//        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
//
//        PlaceScrap scrap1 = PlaceScrap.createScrap(savedUser, savedPlace2, ScrapType.FRIEND);
//
//        PlaceScrap scrap2 = PlaceScrap.createScrap(savedUser, savedPlace3, ScrapType.COMPANY);
//
//        PlaceScrap scrap3 = PlaceScrap.createScrap(savedUser, savedPlace4, ScrapType.COUPLE);
//
//        placeScrapRepository.saveAll(List.of(scrap, scrap1, scrap2, scrap3));
//
//        // when
//        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeQueryRepository.getScrapedPlace(
//                PageRequest.of(0, 10),
//                user.getId(), scrapType);
//
//        // then
//        Assertions.assertThat(scrapedPlace.getContent()).hasSize(expected);
//
//    }
}
